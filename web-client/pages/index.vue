<template>
  <DefaultLayoutWrapper>
    <template #breadcrumbs>
      <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbPage> Home </BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <div class="m-4">
    <Card class="p-4">
      <Form
        v-slot="{ meta, values, validate }"
        as=""
        ref="targetResumeForm"
        keep-values
        :validation-schema="toTypedSchema(formSchema[stepIndex - 1])"
      >
        <Stepper
          v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep, goTo }"
          v-model="stepIndex"
          class="block w-full"
        >
          <form
            class="p-4"
            @submit="
              (e) => {
                e.preventDefault();

                validate();

                if (stepIndex === steps.length && meta.valid) {
                  // values.resume = resumeRef; //TODO: Why??
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
            <div class="flex flex-col gap-4 mt-8">
              <template v-if="stepIndex === 1">
                <!-- <div class="grid grid-cols-1 sm:grid-cols-3 gap-4"> -->
                <FormField v-slot="{ componentField }" name="jobDescription">
                  <FormItem>
                    <FormLabel>Job description</FormLabel>
                    <FormControl>
                      <Textarea
                        v-bind="componentField"
                        placeholder="Paste the job description in here so we can analyze its skills"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <!-- </div> -->
                <Button variant="link" @click="loadSection1TestData">Load Test Data</Button>
              </template>
              <template v-if="stepIndex === 2">
                <FormField v-slot="{ componentField }" name="resume">
                  <FormItem>
                    <FormLabel>Resume (PDF)</FormLabel>
                    <FormControl>
                      <input
                        id="resume"
                        type="file"
                        v-bind="componentField"
                        class="relative m-0 block w-full min-w-0 flex-auto cursor-pointer rounded-md border border-solid border-secondary-500 bg-transparent bg-clip-padding px-3 py-[0.32rem] text-base font-normal text-surface transition duration-300 ease-in-out file:-mx-3 file:-my-[0.32rem] file:me-3 file:cursor-pointer file:overflow-hidden file:rounded-none file:border-0 file:border-e file:border-solid file:border-inherit file:bg-transparent file:px-3 file:py-[0.32rem] file:text-surface focus:border-primary focus:text-gray-700 focus:shadow-inset focus:outline-none dark:border-white/70 dark:text-white file:dark:text-white"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <Button variant="link" @click="loadResumeTestData">Load Test Data</Button>
              </template>
            </div>
            <div class="flex items-center justify-between mt-2">
              <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="prevStep()"> Back </Button>
              <div class="flex items-center gap-3">
                <Button
                  v-if="stepIndex !== steps.length"
                  :type="meta.valid ? 'button' : 'submit'"
                  :disabled="isNextDisabled"
                  size="sm"
                  @click="meta.valid && nextStep()"
                >
                  Next
                </Button>
                <Button v-if="stepIndex === steps.length" size="sm" type="submit" :disabled="saving">
                  <Loader2 v-if="saving" class="w-4 h-4 animate-spin" />
                  Submit
                </Button>
              </div>
            </div>
          </form>
        </Stepper>
      </Form>
    </Card>
    <div class="grid grid-cols-3 gap-4">
      <Card class="my-4">
        <CardHeader>
          <CardTitle>Relevancy Score</CardTitle>
        </CardHeader>
        <CardContent>
          <PieChart :key="results" :match-score="results?.score" />
        </CardContent>
      </Card>
      <Card class="my-4 col-span-2">
        <CardHeader>
          <CardTitle>Keywords</CardTitle>
        </CardHeader>
        <CardContent>
          <ag-grid-vue
            :loading="saving"
            :rowData="results?.jobDescriptionKeywords"
            :columnDefs="colDefs"
            :gridOptions="gridOptions"
            :getRowId="(params) => String(params.data.id)"
            style="height: 888px"
            class="ag-theme-quartz"
          />
        </CardContent>
      </Card>
    </div>
  </div>
</template>
<script setup>
import "ag-grid-community/styles/ag-theme-quartz.css";
import { AgGridVue } from "ag-grid-vue3";
import { Breadcrumb, BreadcrumbItem, BreadcrumbList, BreadcrumbPage } from "@/components/ui/breadcrumb";
import { toTypedSchema } from "@vee-validate/zod";
import { Check, Circle, Dot, ChevronLeft, X, Loader2 } from "lucide-vue-next";
import { h, ref } from "vue";
import { z } from "zod/v4";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormDescription, FormMessage } from "@/components/ui/form";
import { Textarea } from "@/components/ui/textarea";
import {
  Stepper,
  StepperDescription,
  StepperItem,
  StepperSeparator,
  StepperTitle,
  StepperTrigger,
} from "@/components/ui/stepper";
import { toast } from "vue-sonner";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

const targetResumeForm = ref();
let formSchema = [
  z.object({
    jobDescription: z.string("Job description is required."),
  }),
  z.object({
    resume: z.file().max(2_000_000).mime(["application/pdf"]),
  }),
];
const results = ref({});
const stepIndex = ref(1);
const steps = [
  {
    step: 1,
    title: "1. Job Description",
  },
  {
    step: 2,
    title: "2. Resume",
  },
];

const colDefs = ref([
  {
    field: "word",
    headerName: "Key Word or Skill"
  },
  { field: "count" },
  { field: "context" },
  { field: "found" },
]);

const saving = ref(false);

const loadSection1TestData = (e) => {
  e.preventDefault();
  console.log("Load - Job Description");
  targetResumeForm.value.setValues({
    jobDescription: "Hello World!",
  });
};

const base64toFile = (b64, fileName, type) => {
  const fileBytes = atob(b64);
  const byteNumbers = new Array(fileBytes.length);
  for (let i = 0; i < fileBytes.length; i++) {
    byteNumbers[i] = fileBytes.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);

  // Make a Blob and then a File
  const blob = new Blob([byteArray], { type });
  return new File([blob], fileName, { type });
};

const loadResumeTestData = (e) => {
  e.preventDefault();
  console.log("Load - Resume - Test Data");

  // Resume
  const resumePdf =
    "JVBERi0xLjMKJf////8KOCAwIG9iago8PAovVHlwZSAvRXh0R1N0YXRlCi9jYSAxCi9DQSAxCj4+CmVuZG9iago3IDAgb2JqCjw8Ci9UeXBlIC9QYWdlCi9QYXJlbnQgMSAwIFIKL01lZGlhQm94IFswIDAgNTk1LjI4IDg0MS44OV0KL0NvbnRlbnRzIDUgMCBSCi9SZXNvdXJjZXMgNiAwIFIKPj4KZW5kb2JqCjYgMCBvYmoKPDwKL1Byb2NTZXQgWy9QREYgL1RleHQgL0ltYWdlQiAvSW1hZ2VDIC9JbWFnZUldCi9FeHRHU3RhdGUgPDwKL0dzMSA4IDAgUgo+PgovRm9udCA8PAovRjEgOSAwIFIKPj4KPj4KZW5kb2JqCjUgMCBvYmoKPDwKL0xlbmd0aCAxMjEKL0ZpbHRlciAvRmxhdGVEZWNvZGUKPj4Kc3RyZWFtCnicM1QwAEJdQyBhYWKoZ2GpkJzLpe9ebKiQXsyl75JalpmcGuTupJBczGUAVlqcnMeVxlXIZYhNo1MIVNxQwcRAwdzMRM/AzEghBGiim6GCIZCVxhVtY2BgYAjERkBsDMQmQGwK4tspGMQqhHhxuYZwBXIBAOXbICoKZW5kc3RyZWFtCmVuZG9iagoxMSAwIG9iagoocGRmbWFrZSkKZW5kb2JqCjEyIDAgb2JqCihwZGZtYWtlKQplbmRvYmoKMTMgMCBvYmoKKEQ6MjAyNTA4MTkyMTA0NTlaKQplbmRvYmoKMTAgMCBvYmoKPDwKL1Byb2R1Y2VyIDExIDAgUgovQ3JlYXRvciAxMiAwIFIKL0NyZWF0aW9uRGF0ZSAxMyAwIFIKPj4KZW5kb2JqCjE1IDAgb2JqCjw8Ci9UeXBlIC9Gb250RGVzY3JpcHRvcgovRm9udE5hbWUgL0FaWlpaWitOb3RvU2Fucy1SZWd1bGFyCi9GbGFncyA0Ci9Gb250QkJveCBbLTYyMSAtNTA4IDI4MDAgMTA2N10KL0l0YWxpY0FuZ2xlIDAKL0FzY2VudCAxMDY5Ci9EZXNjZW50IC0yOTMKL0NhcEhlaWdodCA3MTQKL1hIZWlnaHQgNTM2Ci9TdGVtViAwCi9Gb250RmlsZTIgMTQgMCBSCj4+CmVuZG9iagoxNiAwIG9iago8PAovVHlwZSAvRm9udAovU3VidHlwZSAvQ0lERm9udFR5cGUyCi9CYXNlRm9udCAvQVpaWlpaK05vdG9TYW5zLVJlZ3VsYXIKL0NJRFN5c3RlbUluZm8gPDwKL1JlZ2lzdHJ5IChBZG9iZSkKL09yZGVyaW5nIChJZGVudGl0eSkKL1N1cHBsZW1lbnQgMAo+PgovRm9udERlc2NyaXB0b3IgMTUgMCBSCi9XIFswIFs2MDAgNjIyIDU2NCA0NzkgNjE4IDkzNV1dCi9DSURUb0dJRE1hcCAvSWRlbnRpdHkKPj4KZW5kb2JqCjE3IDAgb2JqCjw8Ci9MZW5ndGggMjQwCi9GaWx0ZXIgL0ZsYXRlRGVjb2RlCj4+CnN0cmVhbQp4nF1QsW7DIBDd+YobkyEiceV0sZCidPGQtqrbqeqA4bCQakAYD/77wjlNqp4ET+/u3vE4fm6fWmcT8NfoVYcJjHU64uTnqBB6HKxjhwq0VenK6FajDIxncbdMCcfWGQ9NwwD4Wy5PKS6wOWnf47bkXqLGaN0Am49zR5luDuEbR3QJ9kwI0GjyuIsMz3JE4CTdtTrXbVp2WXXveF8CQkX8sFpSXuMUpMIo3YCs2ecQjckhGDr9r3wV9eZvNxSoBXzeaV0RHGuCx4cVVnbU4quM/h1SXikbuf1AzTFm87Q2cl38Woe3zQYfiorODyZdelcKZW5kc3RyZWFtCmVuZG9iago5IDAgb2JqCjw8Ci9UeXBlIC9Gb250Ci9TdWJ0eXBlIC9UeXBlMAovQmFzZUZvbnQgL0FaWlpaWitOb3RvU2Fucy1SZWd1bGFyCi9FbmNvZGluZyAvSWRlbnRpdHktSAovRGVzY2VuZGFudEZvbnRzIFsxNiAwIFJdCi9Ub1VuaWNvZGUgMTcgMCBSCj4+CmVuZG9iago0IDAgb2JqCjw8Cj4+CmVuZG9iagozIDAgb2JqCjw8Ci9UeXBlIC9DYXRhbG9nCi9QYWdlcyAxIDAgUgovTmFtZXMgMiAwIFIKPj4KZW5kb2JqCjEgMCBvYmoKPDwKL1R5cGUgL1BhZ2VzCi9Db3VudCAxCi9LaWRzIFs3IDAgUl0KPj4KZW5kb2JqCjIgMCBvYmoKPDwKL0Rlc3RzIDw8CiAgL05hbWVzIFsKXQo+Pgo+PgplbmRvYmoKMTQgMCBvYmoKPDwKL0xlbmd0aCA3MDgKL0ZpbHRlciAvRmxhdGVEZWNvZGUKPj4Kc3RyZWFtCnicVVJNTBNBFJ6Zlm3EAE673VYo0HbbbSlt+dnuttVSihQoBKHE0NISqKSAVUAIQoLBqBePkhg8mHgyHjiZEE4eOXiqRxN/YogHYySeiIkYTbr4ttREJ/lm5tt973vve5m11fU5pEMxRFB7YW5mFp2uOwC5AB8qfA/gWFzO/+VvAZalmY2VCv8OsK6szlU41sOmu7Z4e77CzyNEnIWltY1TronCZkYYdqJ55906E8rVRX4gjeZQ/Xtw/81X9fyc34+UthVt9ZG2FSgDPaoZAK1P+YjQWXNp+/dB9dFp1j+LqZxuwD3AF0jKA3bVLsoaDKZQfxMi8X+ZWoRf4pMHu9qHUGwaUn6RItLAgJBIbdRpo7Zp/FR5giXlNSmWZJHMQhwMheQgrg6BTdzJcUaWYXQGXsNTnpp4KnbKUkAQsJC5FfL0OF5df7GV3Z9fSCedw6Toy8bDw74aZR+7lA+kOKL87OttC9WDbPDkmGBiRybUXJE12jgOxIJGhuHtgiAFZNlkZ3Q2VdzRl5eURySTFEc9Lcng8HrPxYX+7kEdppYwsUeWBqXJnEF/WU+jm5mJu/HEWMIfd4F78eQY70EVD0JOtqzrkspV1JZdfgJFxE7OpBME3s4YWY4zNRG8F7h6KTTWbJ1s641wYSkw5F7Odow7m+xDLjlc3yWKCeFxoN+hP5c819DqYK1WlnWHvPEhA+2vZfkmrrmRNbjDUH0UPBqJGTwiTHkXX0vKxqhIWbUHWaLEmKjRUjnGx9LezIWuSF8jMZcOBywO2pKK45yyE4v50lFsArFxeFgpsOIqj8sIww+Umy5f1Fv5KprEKFEtibJMnk1m01JopDdtbhfkaMPEQE0V7YhYo1cMVQ1TxD5VUJ7jTHcsm1Le44wnbPGlupVvxDxoceq9fr+XTKBpchPNkAAK4k9IJDfQqGYHjf8Be5ij3wplbmRzdHJlYW0KZW5kb2JqCnhyZWYKMCAxOAowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDE2NTcgMDAwMDAgbiAKMDAwMDAwMTcxNCAwMDAwMCBuIAowMDAwMDAxNTk1IDAwMDAwIG4gCjAwMDAwMDE1NzQgMDAwMDAgbiAKMDAwMDAwMDI5MiAwMDAwMCBuIAowMDAwMDAwMTc1IDAwMDAwIG4gCjAwMDAwMDAwNjUgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAxNDI0IDAwMDAwIG4gCjAwMDAwMDA1NzMgMDAwMDAgbiAKMDAwMDAwMDQ4NSAwMDAwMCBuIAowMDAwMDAwNTExIDAwMDAwIG4gCjAwMDAwMDA1MzcgMDAwMDAgbiAKMDAwMDAwMTc2MSAwMDAwMCBuIAowMDAwMDAwNjQ5IDAwMDAwIG4gCjAwMDAwMDA4NjYgMDAwMDAgbiAKMDAwMDAwMTExMSAwMDAwMCBuIAp0cmFpbGVyCjw8Ci9TaXplIDE4Ci9Sb290IDMgMCBSCi9JbmZvIDEwIDAgUgovSUQgWzwzNzQzNmM4MWNlZDczZGZiNDM0NGVjMjE3NzBhZDk3Mz4gPDM3NDM2YzgxY2VkNzNkZmI0MzQ0ZWMyMTc3MGFkOTczPl0KPj4Kc3RhcnR4cmVmCjI1NDIKJSVFT0YK";
  const resumeFile = base64toFile(resumePdf, "resume.pdf", "application/pdf");

  targetResumeForm.value.setValues({
    resume: resumeFile,
  });
};

const analyzeResume = async (data) => {
  saving.value = true;
  const { fileName: resumeFileName, dataURL: resumeDataURL } = await getFile(data.resume);
  return await $fetch("/api/target-resume-analysis", {
    method: "POST",
    body: {
      metadata: data,
      resumeFileName,
      resumeDataURL,
    },
  });
};

async function onSubmit(values) {
  toast.promise(analyzeResume(values), {
    loading: "Analyzing...",
    success: (data) => {
      saving.value = false;
      results.value = data;
      return `${data.score} toast has been added`;
    },
    error: "Error",
  });
}
</script>
