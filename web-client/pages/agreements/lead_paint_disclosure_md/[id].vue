<template>
  <Agreement
    pdf-type="LEAD_PAINT_ADDENDUM_MD_V1"
    title="Lead Paint Disclosure (Maryland)"
    :agreement-id="agreementId"
    :schema="metaDataSchema"
    :test-data="testData"
  >
    <div class="grid grid-cols-2 gap-4 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="landlord">
          <FormItem>
            <FormLabel>Landlord</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="tenant">
          <FormItem>
            <FormLabel>Tenant</FormLabel>
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
        <FormField v-slot="{ componentField }" name="leasedAddress">
          <FormItem>
            <FormLabel>Leased address</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-6 gap-4 py-2">
      <div class="col-span-1">
        <FormField v-slot="{ componentField }" name="leadKnown">
          <FormItem>
            <FormLabel>Presence of lead paint?</FormLabel>
            <FormControl>
              <RadioGroup v-bind="componentField">
                <div
                  v-for="o in [
                    { id: 'yes', label: 'Yes' },
                    { id: 'no', label: 'No' },
                  ]"
                  class="flex items-center space-x-2"
                >
                  <RadioGroupItem :id="o.id" :value="o.id" />
                  <Label :for="o.id">{{ o.label }}</Label>
                </div>
              </RadioGroup>
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-5">
        <FormField v-slot="{ componentField }" name="explain1">
          <FormItem>
            <FormLabel
              >Known lead-based paint and/or lead-based paint hazards are present in the housing (explain)</FormLabel
            >
            <FormControl>
              <Textarea v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-6 gap-4 py-2">
      <div class="col-span-1">
        <FormField v-slot="{ componentField }" name="documentsProvided">
          <FormItem>
            <FormLabel>Records available to the lessor?</FormLabel>
            <FormControl>
              <RadioGroup v-bind="componentField">
                <div
                  v-for="o in [
                    { id: 'yes', label: 'Yes' },
                    { id: 'no', label: 'No' },
                  ]"
                  class="flex items-center space-x-2"
                >
                  <RadioGroupItem :id="o.id" :value="o.id" />
                  <Label :for="o.id">{{ o.label }}</Label>
                </div>
              </RadioGroup>
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-5">
        <FormField v-slot="{ componentField }" name="explain2">
          <FormItem>
            <FormLabel>
              Lessor has provided the lessee with all available records and reports pertaining to lead-based paint
              and/or lead-based paint hazards in the housing
            </FormLabel>
            <FormControl>
              <Textarea v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <Recipients
      recipientName-1="Landlord name"
      recipientEmail-1="Landlord email"
      recipientName-2="Tenant name"
      recipientEmail-2="Tenant email"
    />
  </Agreement>
</template>
<script setup>
import { z } from "zod/v4";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";

const {
  params: { id: agreementId },
} = useRoute();

let metaDataSchema = z.object({
  landlord: z.string(),
  tenant: z.string(),
  leasedAddress: z.string(),
  leadKnown: z.string(),
  explain1: z.string().optional(),
  documentsProvided: z.string(),
  explain2: z.string().optional(),
  recipients: z.array(
    z.object({
      name: z.string(),
      email: z.email(),
    }),
  ).min(2),
});

const testData = {
  landlord: "Evangelos Poneres",
  tenant: "Stephan Michael Nutty, Kristy Diane Nutty",
  leasedAddress: "10722 LANCEWOOD RD, COCKEYSVILLE, MD 21030",
  leadKnown: "no",
  documentsProvided: "no",
  explain1: "Hello",
  explain2: "World",
};
</script>
