<script setup lang="ts">
import { toTypedSchema } from "@vee-validate/zod";
import { Check, Circle, Dot, ChevronLeft } from "lucide-vue-next";
import { h, ref } from "vue";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import {
  Stepper,
  StepperDescription,
  StepperItem,
  StepperSeparator,
  StepperTitle,
  StepperTrigger,
} from "@/components/ui/stepper";
import { Toaster } from "@/components/ui/sonner";
import { toast } from "vue-sonner";

definePageMeta({
  layout: "onboarding",
});

const formSchema = [
  z.object({
    firstName: z.string(),
    middleName: z.string(),
    lastName: z.string(),
    dob: z.string(),
    specialty: z.string(),
    location: z.string(),
  }),
  z
    .object({
      password: z.string().min(2).max(50),
      confirmPassword: z.string(),
    })
    .refine(
      (values) => {
        return values.password === values.confirmPassword;
      },
      {
        message: "Passwords must match!",
        path: ["confirmPassword"],
      },
    ),
  z.object({
    employmentType: z.union([z.literal("full_time"), z.literal("part_time")]),
  }),
];

const stepIndex = ref(1);
const steps = [
  {
    step: 1,
    title: "Details",
    description: "Provide your medical details",
  },
  {
    step: 2,
    title: "Credentials",
    description: "Provide your medical credentials",
  },
  {
    step: 3,
    title: "Scheduling",
    description: "Tell us your availability",
  },
];

function onSubmit(values: any) {
  toast.success("Thank you!", {
    description: h(
      "pre",
      { class: "mt-2 w-[340px] rounded-md p-4" },
      h("code", { class: "text-white" }, JSON.stringify(values, null, 2)),
    ),
  });
}
</script>

<template>
  <Toaster />
  <NuxtLink to="/" class="flex p-2">
    <ChevronLeft />
    Back
  </NuxtLink>
  <Form
    v-slot="{ meta, values, validate }"
    as=""
    keep-values
    :validation-schema="toTypedSchema(formSchema[stepIndex - 1])"
  >
    <Stepper v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep }" v-model="stepIndex" class="block w-full">
      <form
        class="p-4"
        @submit="
          (e) => {
            e.preventDefault();
            validate();

            if (stepIndex === steps.length && meta.valid) {
              onSubmit(values);
            }
          }
        "
      >
        <div class="flex w-full flex-start gap-2">
          <StepperItem
            v-for="step in steps"
            :key="step.step"
            v-slot="{ state }"
            class="relative flex w-full flex-col items-center justify-center"
            :step="step.step"
          >
            <StepperSeparator
              v-if="step.step !== steps[steps.length - 1].step"
              class="absolute left-[calc(50%+20px)] right-[calc(-50%+10px)] top-5 block h-0.5 shrink-0 rounded-full bg-muted group-data-[state=completed]:bg-primary"
            />

            <StepperTrigger as-child>
              <Button
                :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'"
                size="icon"
                class="z-10 rounded-full shrink-0"
                :class="[state === 'active' && 'ring-2 ring-ring ring-offset-2 ring-offset-background']"
                :disabled="state !== 'completed' && !meta.valid"
              >
                <Check v-if="state === 'completed'" class="size-5" />
                <Circle v-if="state === 'active'" />
                <Dot v-if="state === 'inactive'" />
              </Button>
            </StepperTrigger>

            <div class="mt-5 flex flex-col items-center text-center">
              <StepperTitle
                :class="[state === 'active' && 'text-primary']"
                class="text-sm font-semibold transition lg:text-base"
              >
                {{ step.title }}
              </StepperTitle>
              <StepperDescription
                :class="[state === 'active' && 'text-primary']"
                class="sr-only text-xs text-muted-foreground transition md:not-sr-only lg:text-sm"
              >
                {{ step.description }}
              </StepperDescription>
            </div>
          </StepperItem>
        </div>
        <div class="flex flex-col gap-4 mt-4">
          <template v-if="stepIndex === 1">
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <FormField v-slot="{ componentField }" name="firstName">
                <FormItem>
                  <FormLabel>First name</FormLabel>
                  <FormControl>
                    <Input type="firstName" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <FormField v-slot="{ componentField }" name="middleName">
                <FormItem>
                  <FormLabel>Middle name</FormLabel>
                  <FormControl>
                    <Input type="middleName" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <FormField v-slot="{ componentField }" name="lastName">
                <FormItem>
                  <FormLabel>Last name</FormLabel>
                  <FormControl>
                    <Input type="lastName" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-4 gap-4">
              <FormField v-slot="{ componentField }" name="dob">
                <FormItem>
                  <FormLabel>Date of birth</FormLabel>
                  <FormControl>
                    <DatePicker v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <div class="col-span-2">
                <FormField v-slot="{ componentField }" name="specialty">
                  <FormItem>
                    <FormLabel>Specialty</FormLabel>
                    <Select v-bind="componentField">
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select a specialty" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectGroup>
                          <SelectItem value="anesthesiology"> Anesthesiology </SelectItem>
                          <SelectItem value="crna"> Certified Registered Nurse Anesthetist (CRNA) </SelectItem>
                          <SelectItem value="ems"> Emergency Medicine Specialists </SelectItem>
                          <SelectItem value="rn"> Registered Nurse (RN) </SelectItem>
                        </SelectGroup>
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>

              <div class="col-start-4">
                <FormField v-slot="{ componentField }" name="location">
                  <FormItem>
                    <FormLabel>Location</FormLabel>
                    <FormControl>
                      <Input type="location" v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
            </div>
          </template>

          <template v-if="stepIndex === 2">
            <FormField v-slot="{ componentField }" name="password">
              <FormItem>
                <FormLabel>Password</FormLabel>
                <FormControl>
                  <Input type="password" v-bind="componentField" />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>

            <FormField v-slot="{ componentField }" name="confirmPassword">
              <FormItem>
                <FormLabel>Confirm Password</FormLabel>
                <FormControl>
                  <Input type="password" v-bind="componentField" />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
          </template>

          <template v-if="stepIndex === 3">
            <FormField v-slot="{ componentField }" name="employmentType">
              <FormItem>
                <FormLabel>Employment Type</FormLabel>

                <Select v-bind="componentField">
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select an employment type" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem value="full_time"> Full Time </SelectItem>
                      <SelectItem value="part_time"> Part Time </SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            </FormField>
          </template>
        </div>

        <div class="flex items-center justify-between mt-4">
          <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="prevStep()"> Back </Button>
          <div class="flex items-center gap-3">
            <Button
              v-if="stepIndex !== 3"
              :type="meta.valid ? 'button' : 'submit'"
              :disabled="isNextDisabled"
              size="sm"
              @click="meta.valid && nextStep()"
            >
              Next
            </Button>
            <Button v-if="stepIndex === 3" size="sm" type="submit"> Submit </Button>
          </div>
        </div>
      </form>
    </Stepper>
  </Form>
</template>
