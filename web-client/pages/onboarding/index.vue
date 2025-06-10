<script setup lang="ts">
import { toTypedSchema } from "@vee-validate/zod";
import { Check, Circle, Dot, ChevronLeft } from "lucide-vue-next";
import { h, ref } from "vue";
import { z } from "zod/v4";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
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
  ssr: false,
  layout: "onboarding",
});

let formSchema = [
  z.object({
    firstName: z.string(),
    middleName: z.string(),
    lastName: z.string(),
    dob: z.string(),
    specialty: z.string(),
    stateLicense: z.string(),
    uncomfortableProcedures: z.string(),
    haveYouDoneLocumsBefore: z.string(),
    activeCertifications: z.string(),
    malpractice: z.string()
  }),
  z.object({
    resume: z.file().max(2_000_000).mime(["application/pdf"]),
  }),
  z.object({
    employmentType: z.union([z.literal("full_time"), z.literal("part_time")]),
  }),
];

const stepIndex = ref(1);
const steps = [
  {
    step: 1,
    title: "Details",
  },
  {
    step: 2,
    title: "Credentials",
  },
  {
    step: 3,
    title: "Scheduling",
  },
];

const resumeRef = ref();
const resumeFileNameRef = ref();

const handleResumeChange = (e) => {
  const file = e.target.files[0];
  resumeFileNameRef.value = e.target.files[0].name;

  const reader = new FileReader();
  reader.onloadend = () => {
    resumeRef.value = reader.result;
  };
  reader.readAsDataURL(file);
};

const saveOnboarding = (data) => {
  $fetch('/api/onboarding', {
    method: "POST",
    body: {
      onboarding: data,
      resumeDataURL: resumeRef.value,
      resumeFileName: resumeFileNameRef.value,
    }
  });
}

function onSubmit(values: any) {
  saveOnboarding(values);
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
              values.resume = resumeRef;
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
            <div class="grid grid-cols-1 sm:grid-cols-5 gap-4">
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
              <div  class="col-span-2">
                <FormField v-slot="{ componentField }" name="stateLicense">
                  <FormItem>
                    <FormLabel>What state do you have an active license?</FormLabel>
                    <FormControl>
                      <Input type="stateLicense" v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-5">
                <FormField v-slot="{ componentField }" name="uncomfortableProcedures">
                  <FormItem>
                    <FormLabel>Are there any procedures/cases you are uncomfortable seeing/doing?</FormLabel>
                    <FormControl>
                      <Textarea v-bind="componentField" />
                    </FormControl>
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-5">
                <FormField v-slot="{ componentField }" name="haveYouDoneLocumsBefore">
                  <FormItem>
                    <FormLabel>Have you ever done locums before? If so, when and where?</FormLabel>
                    <FormControl>
                      <Textarea v-bind="componentField" />
                    </FormControl>
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-5">
                <FormField v-slot="{ componentField }" name="activeCertifications">
                  <FormItem>
                    <FormLabel>Do you have any active certifications? (i.e. BLS, ACLS, NRP, etc.)</FormLabel>
                    <FormControl>
                      <Textarea v-bind="componentField" />
                    </FormControl>
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-5">
                <FormField v-slot="{ componentField }" name="malpractice">
                  <FormItem>
                    <FormLabel>Any dropped, pending or settled malpractice cases?</FormLabel>
                    <FormControl>
                      <Textarea v-bind="componentField" />
                    </FormControl>
                  </FormItem>
                </FormField>
              </div>
            </div>
          </template>

          <template v-if="stepIndex === 2">
            <FormField v-slot="{ componentField }" name="resume">
              <FormItem>
                <FormLabel>Resume (PDF)</FormLabel>
                <FormControl>
                  <div>
                    <input
                      id="resume"
                      type="file"
                      v-bind="componentField"
                      @change="handleResumeChange"
                      class="relative m-0 block w-full min-w-0 flex-auto cursor-pointer rounded-md border border-solid border-secondary-500 bg-transparent bg-clip-padding px-3 py-[0.32rem] text-base font-normal text-surface transition duration-300 ease-in-out file:-mx-3 file:-my-[0.32rem] file:me-3 file:cursor-pointer file:overflow-hidden file:rounded-none file:border-0 file:border-e file:border-solid file:border-inherit file:bg-transparent file:px-3 file:py-[0.32rem] file:text-surface focus:border-primary focus:text-gray-700 focus:shadow-inset focus:outline-none dark:border-white/70 dark:text-white file:dark:text-white"
                    />
                  </div>
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
