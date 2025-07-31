<script setup lang="ts">
import { DateFormatter, type DateValue, getLocalTimeZone } from "@internationalized/date";
import { Button } from "@/components/ui/button";
import { CalendarIcon, X } from "lucide-vue-next";

import { ref } from "vue";
import { cn } from "@/lib/utils";
import { Calendar } from "@/components/ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { PopoverClose } from "reka-ui";

const df = new DateFormatter("en-US", {
  dateStyle: "long",
});
const { minValue } = defineProps(["minValue"]);
const emit = defineEmits(["update:modelValue"]);
const value = ref<DateValue>();
</script>

<template>
  <Popover>
    <PopoverTrigger as-child>
      <Button variant="outline" :class="cn('justify-start text-left font-normal', !value && 'text-muted-foreground')">
        <CalendarIcon class="mr-2 h-4 w-4" />
        {{ value ? df.format(value.toDate(getLocalTimeZone())) : "Pick a date" }}
      </Button>
    </PopoverTrigger>
    <PopoverContent class="w-auto p-0">
      <PopoverClose
        class="rounded-full p-[2px] h-[18px] w-[18px] inline-flex items-center justify-center absolute top-[6px] right-[6px] hover:bg-gray-100 cursor-pointer"
      >
        <X />
      </PopoverClose>
      <Calendar
        v-model="value"
        initial-focus
        :minValue="minValue"
        @update:model-value="
          (v) => {
            emit('update:modelValue', value.toDate(getLocalTimeZone()).toISOString().split('T')[0]);
          }
        "
        class="p-2 pt-8"
      />
    </PopoverContent>
  </Popover>
</template>
