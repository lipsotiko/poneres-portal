<template>
  <Agreement
    pdf-type="PROPERTY_MANAGEMENT_AGREEMENT_V1"
    title="Property Management Agreement"
    :agreement-id="agreementId"
    :schema="metaDataSchema"
    :test-data="testData"
  >
    <div class="grid grid-cols-4 gap-4 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="agreementDate">
          <FormItem>
            <FormLabel>Agreement date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="date" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="maxExpensesInDollars">
          <FormItem>
            <FormLabel>Max expenses ($)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="compensationPercentage">
          <FormItem>
            <FormLabel>Compensation (%)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="noticeDays">
          <FormItem>
            <FormLabel>Termination notice days</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-4 gap-4 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="owner">
          <FormItem>
            <FormLabel>Owner</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="manager">
          <FormItem>
            <FormLabel>Manager</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="termStartDate">
          <FormItem>
            <FormLabel>Start date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="month" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="termEndDate">
          <FormItem>
            <FormLabel>End date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="month" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-2 gap-4 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="ownerAddress">
          <FormItem>
            <FormLabel>Owner Notice Address</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="managerAddress">
          <FormItem>
            <FormLabel>Manager Notice Address</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="propertyAddress">
          <FormItem>
            <FormLabel>Property address</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="addendum">
          <FormItem>
            <FormLabel>Addendum</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <Recipients
      recipientName-1="Manager name"
      recipientEmail-1="Manager email"
      recipientName-2="Owner name"
      recipientEmail-2="Owner email"
    />
  </Agreement>
</template>
<script setup>
import { z } from "zod/v4";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";

const {
  params: { id: agreementId },
} = useRoute();
const dayjs = useDayjs();

const metaDataSchema = z.object({
  agreementDate: z.string(),
  owner: z.string(),
  manager: z.string(),
  propertyAddress: z.string(),
  maxExpensesInDollars: z.number(),
  compensationPercentage: z.number(),
  noticeDays: z.number(),
  addendum: z.string(),
  termStartDate: z.string(),
  termEndDate: z.string(),
  ownerAddress: z.string(),
  managerAddress: z.string(),
  recipients: z.array(
    z.object({
      name: z.string(),
      email: z.email(),
    }),
  ).min(2),
});

const testData = {
  agreementDate: dayjs().format("YYYY-MM-DD"),
  owner: "Hello",
  manager: "World",
  propertyAddress: "123 Cool Ln, Baltimore, MD 22111",
  maxExpensesInDollars: 2000,
  compensationPercentage: 10,
  noticeDays: 60,
  addendum: "Test",
  termStartDate: dayjs().add(-2, "month").format("YYYY-MM"),
  termEndDate: dayjs().add(2, "month").format("YYYY-MM"),
  ownerAddress: "222 Cool Ln, Baltimore, MD 22111",
  managerAddress: "333 Cool Ln, Baltimore, MD 22111",
};
</script>
