<template>
  <FormFieldArray name="recipients" v-slot="{ fields, push, remove }">
    <fieldset class="InputGroup" v-for="(field, idx) in fields" :key="field.key">
      <div class="grid grid-cols-2 gap-4">
        <FormField v-slot="{ componentField }" :name="`recipients[${idx}].name`">
          <FormItem class="py-2">
            <FormLabel>
              <span v-if="idx === 0">{{ recipientName1 }}</span>
              <span v-else>{{ recipientName2 }} {{ idx }}</span>
            </FormLabel>
            <Input v-bind="componentField" />
            <FormMessage />
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" :name="`recipients[${idx}].email`">
          <FormItem class="py-2">
            <FormLabel>
              <span v-if="idx === 0">{{ recipientEmail1 }}</span>
              <span v-else>{{ recipientEmail2 }} {{ idx }}</span>
            </FormLabel>
            <div class="flex">
              <Input v-bind="componentField" />
              <div v-if="idx >= 2" class="mx-2">
                <Button variant="outline" size="icon" @click="remove(idx)">
                  <X class="w-4 h-4" />
                </Button>
              </div>
            </div>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </fieldset>
    <Button type="button" variant="outline" @click="push({ name: undefined, email: undefined })">
      Add Recipient +
    </Button>
  </FormFieldArray>
</template>
<script setup>
import { FormControl, FormField, FormItem, FormLabel, FormMessage, FormFieldArray } from "@/components/ui/form";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { X } from "lucide-vue-next";

const { recipientName1, recipientEmail1, recipientName2, recipientEmail2 } = defineProps([
  "recipientName-1",
  "recipientEmail-1",
  "recipientName-2",
  "recipientEmail-2",
]);

const removeRecipient = (index) => {
  if (recipientsSchema.value.recipients.length === 1) {
    return;
  }
  recipientsSchema.value.recipients.splice(index, 1);
};
</script>
