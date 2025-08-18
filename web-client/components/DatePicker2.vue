<script setup lang="ts">
import { DateFormatter, type DateValue, getLocalTimeZone, today, CalendarDate } from "@internationalized/date";
import { Button } from "@/components/ui/button";
import {
  CalendarRoot,
  type CalendarRootEmits,
  type CalendarRootProps,
  useDateFormatter,
  useForwardPropsEmits,
} from "reka-ui";
import { createDecade, createYear, toDate } from "reka-ui/date";
import { computed, type HTMLAttributes, type Ref } from "vue";
import { cn } from "@/lib/utils";
import {
  CalendarCell,
  CalendarCellTrigger,
  CalendarGrid,
  CalendarGridBody,
  CalendarGridHead,
  CalendarGridRow,
  CalendarHeadCell,
  CalendarHeader,
  CalendarHeading,
} from "@/components/ui/calendar";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { PopoverClose } from "reka-ui";
import { CalendarIcon, X } from "lucide-vue-next";

const props = defineProps<CalendarRootProps & { class?: HTMLAttributes["class"] }>();
const emits = defineEmits<CalendarRootEmits>();

const delegatedProps = computed(() => {
  const { class: _, placeholder: __, ...delegated } = props;

  return delegated;
});

const current = today(getLocalTimeZone());
const defaultMaxDate = new CalendarDate(current.year - 18, current.month, current.day);

const value = ref<DateValue>(props.defaultValue);
const forwarded = useForwardPropsEmits(delegatedProps, emits);

const formatter = useDateFormatter("en");
const df = new DateFormatter("en-US", {
  dateStyle: "medium",
});
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
      <CalendarRoot
        v-slot="{ date, grid, weekDays }"
        v-model:placeholder="value"
        v-bind="forwarded"
        class="p-2 pt-8"
      >
        <CalendarHeader>
          <CalendarHeading class="flex w-full items-center justify-between gap-2">
            <Select
              :default-value="value?.month.toString()"
              @update:model-value="
                (v) => {
                  if (!v) return;
                  if (!value) {
                    value = defaultMaxDate.set({
                      month: Number(v),
                      year: defaultMaxDate.year,
                    });
                    return;
                  }
                  if (Number(v) === value?.month) return;
                  value = value.set({
                    month: Number(v),
                  });
                }
              "
            >
              <SelectTrigger aria-label="Select month" class="w-[134px]">
                <SelectValue placeholder="Select month" />
              </SelectTrigger>
              <SelectContent class="max-h-[200px]">
                <SelectItem
                  v-for="month in createYear({ dateObj: date })"
                  :key="month.toString()"
                  :value="month.month.toString()"
                >
                  {{ formatter.custom(toDate(month), { month: "long" }) }}
                </SelectItem>
              </SelectContent>
            </Select>
            <Select
              :default-value="value?.year.toString()"
              @update:model-value="
                (v) => {
                  if (!v) return;
                  if (!value) {
                    value = defaultMaxDate.set({
                      month: defaultMaxDate.month,
                      year: Number(v),
                    });
                    return;
                  }
                  if (Number(v) === value?.year) return;
                  value = value.set({
                    year: Number(v),
                  });
                }
              "
            >
              <SelectTrigger aria-label="Select year" class="w-[122px]">
                <SelectValue placeholder="Select year" />
              </SelectTrigger>
              <SelectContent class="max-h-[200px]">
                <SelectItem
                  v-for="yearValue in Array.from({ length: 80 }, (_, i) => i + defaultMaxDate.year - 79).reverse()"
                  :key="yearValue.toString()"
                  :value="yearValue.toString()"
                >
                  {{ yearValue }}
                </SelectItem>
              </SelectContent>
            </Select>
          </CalendarHeading>
        </CalendarHeader>
        <div class="flex justify-center pt-4">
          <div>
            <CalendarGrid v-for="month in grid" :key="month.value.toString()">
              <CalendarGridHead>
                <CalendarGridRow>
                  <CalendarHeadCell v-for="day in weekDays" :key="day">
                    {{ day }}
                  </CalendarHeadCell>
                </CalendarGridRow>
              </CalendarGridHead>
              <CalendarGridBody class="grid">
                <CalendarGridRow
                  v-for="(weekDates, index) in month.rows"
                  :key="`weekDate-${index}`"
                  class="mt-2 w-full"
                >
                  <CalendarCell v-for="weekDate in weekDates" :key="weekDate.toString()" :date="weekDate">
                    <CalendarCellTrigger :day="weekDate" :month="month.value" />
                  </CalendarCell>
                </CalendarGridRow>
              </CalendarGridBody>
            </CalendarGrid>
          </div>
        </div>
      </CalendarRoot>
    </PopoverContent>
  </Popover>
</template>
