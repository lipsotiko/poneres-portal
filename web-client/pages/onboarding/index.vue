<script setup lang="ts">
import { toTypedSchema } from "@vee-validate/zod";
import { Check, Circle, Dot, ChevronLeft, X, Loader2 } from "lucide-vue-next";
import { h, ref } from "vue";
import { z } from "zod/v4";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormDescription, FormMessage, FormFieldArray } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Checkbox } from "@/components/ui/checkbox";
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
import { toast } from "vue-sonner";
import { getLocalTimeZone, today, CalendarDate } from "@internationalized/date";

const dayjs = useDayjs();

definePageMeta({
  layout: "onboarding",
});

const onboardingForm = ref();

let formSchema = [
  z.object({
    firstName: z.string("First name is required."),
    middleName: z.string().optional(),
    lastName: z.string("Last name is required."),
    gender: z.string("Gender is required."),
    dob: z.string("DOB is required."),
    citizenshipStatus: z.string("Citizenship status is required."),
    specialty: z.string("Specialty is required."),
    npi: z.string("NPI is required."),
    location: z.string("Location is required."),
    homeAddress: z.object({
      addressLine1: z.string("Address is required."),
      addressLine2: z.string().optional(),
      city: z.string("City is required."),
      state: z.string("State is required."),
      zip: z.string("Zip Code is required."),
    }),
    mailingAddress: z.object({
      addressLine1: z.string("Address is required."),
      addressLine2: z.string().optional(),
      city: z.string("City is required."),
      state: z.string("State is required."),
      zip: z.string("Zip Code is required."),
    }),
    employmentType: z.string(),
    schedulePreferences: z.array(z.string()).min(1),
  }),
  z.object({
    resume: z.file().max(2_000_000).mime(["application/pdf"]),
    licenseFiles: z.array(
      z.object({
        state: z.string(),
        licenseNumber: z.string(),
        expirationDate: z.string(),
        license: z.file().max(2_000_000).mime(["application/pdf"]),
      }),
    ).min(1),
    govId: z.file().max(2_000_000).mime(["application/pdf", "image/jpeg", "image/png"]),
  }),
  z.object({
    educationDetails: z.array(
      z.object({
        schoolName: z.string(),
        degree: z.string(),
        graduationDate: z.string(),
      })
    ).min(1)
  }),
  z.object({
    employmentHistory: z.array(
      z.object({
        employerName: z.string(),
        startDate: z.string(),
        endDate: z.string(),
        positionDetails: z.string(),
        supervisorName: z.string(),
        supervisorContact: z.string(),
      })
    ).min(1)
  }),
  z.object({
    attestationOne: z.boolean().refine((attestationOne) => attestationOne === true, {
      error: "Attestation is required.",
    }),
    attestationTwo: z.boolean().refine((attestationTwo) => attestationTwo === true, {
      error: "Attestation is required.",
    }),
    signature: z.string(),
    signatureDate: z.string()
  }),
];

const stepIndex = ref(1);
const steps = [
  {
    step: 1,
    title: "Basic Information",
  },
  {
    step: 2,
    title: "Credentials",
  },
  {
    step: 3,
    title: "Education",
  },
  {
    step: 4,
    title: "Work History",
  },
  {
    step: 5,
    title: "Review & Attestation",
  },
];

const getFile = async (f) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => {
      resolve({
        dataURL: reader.result,
        fileName: f.name,
      });
    };
    reader.readAsDataURL(f);
  });
};

const saving = ref(false);
const submitted = ref(false);

const saveOnboarding = async (data) => {
  saving.value = true;
  const { fileName: resumeFileName, dataURL: resumeDataURL } = await getFile(data.resume);
  const { fileName: govIdFileName, dataURL: govIdDataURL } = await getFile(data.govId);

  const licenseFiles = await Promise.all(data.licenseFiles.map((l) => getFile(l.license)));

  await $fetch("/api/onboarding", {
    method: "POST",
    body: {
      onboarding: data,
      resumeFileName,
      resumeDataURL,
      govIdFileName,
      govIdDataURL,
      licenseFiles,
    },
  });
};

async function onSubmit(values: any) {
  await saveOnboarding(values);
  submitted.value = true;
  toast.success("Thank you!", {
    description: h("div", [h("div", "Your onboarding has started."), h("div", "You will be redirected shortly...")]),
    onAutoClose: () => {
      navigateTo("/");
    },
  });
}

let initialData = {
  licenseFiles: [
    {
      state: undefined,
      licenseNumber: undefined,
      expirationDate: undefined,
    },
  ],
  educationDetails: [{
    schoolName: undefined,
    graduationDate: undefined,
    degree: undefined,
  }],
  employmentHistory: [{
    employerName: undefined,
    startDate: undefined,
    endDate: undefined,
    positionDetails: undefined,
    supervisorName: undefined,
    supervisorContact: undefined,
  }],
  schedulePreferences: [],
  attestationOne: false,
  attestationTwo: false,
  signatureDate: dayjs().format('DD/MM/YYYY')
};

// initialData = {
//   licenseFiles: [
//     {
//       state: "MD",
//       licenseNumber: "123456789",
//       expirationDate: "2029-01-02",
//       license: {},
//     },
//   ],
//   educationDetails: [
//     {
//       schoolName: "Towson University",
//       graduationDate: "2017-12-13",
//       degree: "BS",
//     },
//     {
//       schoolName: "Drexel University",
//       graduationDate: "2020-12-13",
//       degree: "MD",
//     },
//   ],
//   employmentHistory: [
//     {
//       employerName: "General Dynamics",
//       startDate: "2020-03-02",
//       endDate: "2023-12-13",
//       positionDetails:
//         "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
//       supervisorName: "Jane Smith",
//       supervisorContact: "444-333-1111",
//     },
//     {
//       employerName: "Pivotal Labs",
//       startDate: "2023-12-03",
//       endDate: "2024-6-11",
//       positionDetails:
//         "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
//       supervisorName: "Jack Jones",
//       supervisorContact: "444-333-2222",
//     },
//   ],
//   schedulePreferences: ["day", "evening"],
//   attestationOne: false,
//   attestationTwo: false,
//   signatureDate: "02/09/2025",
//   firstName: "Elias",
//   middleName: "Sevastianos",
//   lastName: "Poneres",
//   gender: "Male",
//   dob: "2007-01-02",
//   citizenshipStatus: "US Citizen",
//   specialty: "Anesthesiology",
//   npi: "123456789",
//   location: "MD",
//   homeAddress: {
//     addressLine1: "111 Beech Ave.",
//     city: "Baltimore",
//     state: "MD",
//     zip: "21211",
//   },
//   mailingAddress: {
//     addressLine1: "222 Beech Ave.",
//     city: "Baltimore",
//     state: "MD",
//     zip: "21211",
//   },
//   employmentType: "Short or Long Term",
//   resume: {},
//   govId: {},
// };

const current = today(getLocalTimeZone());
const maxDateDob = new CalendarDate(current.year - 18, current.month, current.day);

const loadSection1TestData = (e) => {
  e.preventDefault();
  console.log("Load - Basic Info. - Test Data");
  onboardingForm.value.setValues({
    firstName: 'Elias',
    middleName: 'Sevastianos',
    lastName: 'Poneres',
    gender: 'Male',
    dob: '2007-01-02',
    citizenshipStatus: 'US Citizen',
    specialty: 'Anesthesiology',
    npi: '123456789',
    location: 'MD',
    homeAddress: {
      addressLine1: "111 Beech Ave.",
      city: 'Baltimore',
      state: 'MD',
      zip: '21211'
    },
    mailingAddress: {
      addressLine1: "222 Beech Ave.",
      city: 'Baltimore',
      state: 'MD',
      zip: '21211'
    },
    employmentType: 'Short or Long Term',
    schedulePreferences: ['day', 'evening']
  })
}

const loadEducationTestData = (e) => {
  e.preventDefault();
  console.log("Load - Education. - Test Data");
  onboardingForm.value.setValues({
    educationDetails: [{
      schoolName: 'Towson University',
      graduationDate: '2017-12-13',
      degree: 'BS'
    }, {
      schoolName: 'Drexel University',
      graduationDate: '2020-12-13',
      degree: 'MD'
    }]
  });
}

const loadWorkHistoryTestData = (e) => {
  e.preventDefault();
  console.log("Load - Work History - Test Data");
  onboardingForm.value.setValues({
    employmentHistory: [{
      employerName: 'General Dynamics',
      startDate: '2020-03-02',
      endDate: '2023-12-13',
      positionDetails: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
      supervisorName: 'Jane Smith',
      supervisorContact: '444-333-1111'
    }, {
      employerName: 'Pivotal Labs',
      startDate: '2023-12-03',
      endDate: '2024-6-11',
      positionDetails: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
      supervisorName: 'Jack Jones',
      supervisorContact: '444-333-2222'
    }]
  });
}

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
}

const loadCredentialsTestData = (e) => {
  e.preventDefault();
  console.log("Load - Credentials - Test Data");

  // Resume
  const resumePdf = 'JVBERi0xLjMKJf////8KOCAwIG9iago8PAovVHlwZSAvRXh0R1N0YXRlCi9jYSAxCi9DQSAxCj4+CmVuZG9iago3IDAgb2JqCjw8Ci9UeXBlIC9QYWdlCi9QYXJlbnQgMSAwIFIKL01lZGlhQm94IFswIDAgNTk1LjI4IDg0MS44OV0KL0NvbnRlbnRzIDUgMCBSCi9SZXNvdXJjZXMgNiAwIFIKPj4KZW5kb2JqCjYgMCBvYmoKPDwKL1Byb2NTZXQgWy9QREYgL1RleHQgL0ltYWdlQiAvSW1hZ2VDIC9JbWFnZUldCi9FeHRHU3RhdGUgPDwKL0dzMSA4IDAgUgo+PgovRm9udCA8PAovRjEgOSAwIFIKPj4KPj4KZW5kb2JqCjUgMCBvYmoKPDwKL0xlbmd0aCAxMjEKL0ZpbHRlciAvRmxhdGVEZWNvZGUKPj4Kc3RyZWFtCnicM1QwAEJdQyBhYWKoZ2GpkJzLpe9ebKiQXsyl75JalpmcGuTupJBczGUAVlqcnMeVxlXIZYhNo1MIVNxQwcRAwdzMRM/AzEghBGiim6GCIZCVxhVtY2BgYAjERkBsDMQmQGwK4tspGMQqhHhxuYZwBXIBAOXbICoKZW5kc3RyZWFtCmVuZG9iagoxMSAwIG9iagoocGRmbWFrZSkKZW5kb2JqCjEyIDAgb2JqCihwZGZtYWtlKQplbmRvYmoKMTMgMCBvYmoKKEQ6MjAyNTA4MTkyMTA0NTlaKQplbmRvYmoKMTAgMCBvYmoKPDwKL1Byb2R1Y2VyIDExIDAgUgovQ3JlYXRvciAxMiAwIFIKL0NyZWF0aW9uRGF0ZSAxMyAwIFIKPj4KZW5kb2JqCjE1IDAgb2JqCjw8Ci9UeXBlIC9Gb250RGVzY3JpcHRvcgovRm9udE5hbWUgL0FaWlpaWitOb3RvU2Fucy1SZWd1bGFyCi9GbGFncyA0Ci9Gb250QkJveCBbLTYyMSAtNTA4IDI4MDAgMTA2N10KL0l0YWxpY0FuZ2xlIDAKL0FzY2VudCAxMDY5Ci9EZXNjZW50IC0yOTMKL0NhcEhlaWdodCA3MTQKL1hIZWlnaHQgNTM2Ci9TdGVtViAwCi9Gb250RmlsZTIgMTQgMCBSCj4+CmVuZG9iagoxNiAwIG9iago8PAovVHlwZSAvRm9udAovU3VidHlwZSAvQ0lERm9udFR5cGUyCi9CYXNlRm9udCAvQVpaWlpaK05vdG9TYW5zLVJlZ3VsYXIKL0NJRFN5c3RlbUluZm8gPDwKL1JlZ2lzdHJ5IChBZG9iZSkKL09yZGVyaW5nIChJZGVudGl0eSkKL1N1cHBsZW1lbnQgMAo+PgovRm9udERlc2NyaXB0b3IgMTUgMCBSCi9XIFswIFs2MDAgNjIyIDU2NCA0NzkgNjE4IDkzNV1dCi9DSURUb0dJRE1hcCAvSWRlbnRpdHkKPj4KZW5kb2JqCjE3IDAgb2JqCjw8Ci9MZW5ndGggMjQwCi9GaWx0ZXIgL0ZsYXRlRGVjb2RlCj4+CnN0cmVhbQp4nF1QsW7DIBDd+YobkyEiceV0sZCidPGQtqrbqeqA4bCQakAYD/77wjlNqp4ET+/u3vE4fm6fWmcT8NfoVYcJjHU64uTnqBB6HKxjhwq0VenK6FajDIxncbdMCcfWGQ9NwwD4Wy5PKS6wOWnf47bkXqLGaN0Am49zR5luDuEbR3QJ9kwI0GjyuIsMz3JE4CTdtTrXbVp2WXXveF8CQkX8sFpSXuMUpMIo3YCs2ecQjckhGDr9r3wV9eZvNxSoBXzeaV0RHGuCx4cVVnbU4quM/h1SXikbuf1AzTFm87Q2cl38Woe3zQYfiorODyZdelcKZW5kc3RyZWFtCmVuZG9iago5IDAgb2JqCjw8Ci9UeXBlIC9Gb250Ci9TdWJ0eXBlIC9UeXBlMAovQmFzZUZvbnQgL0FaWlpaWitOb3RvU2Fucy1SZWd1bGFyCi9FbmNvZGluZyAvSWRlbnRpdHktSAovRGVzY2VuZGFudEZvbnRzIFsxNiAwIFJdCi9Ub1VuaWNvZGUgMTcgMCBSCj4+CmVuZG9iago0IDAgb2JqCjw8Cj4+CmVuZG9iagozIDAgb2JqCjw8Ci9UeXBlIC9DYXRhbG9nCi9QYWdlcyAxIDAgUgovTmFtZXMgMiAwIFIKPj4KZW5kb2JqCjEgMCBvYmoKPDwKL1R5cGUgL1BhZ2VzCi9Db3VudCAxCi9LaWRzIFs3IDAgUl0KPj4KZW5kb2JqCjIgMCBvYmoKPDwKL0Rlc3RzIDw8CiAgL05hbWVzIFsKXQo+Pgo+PgplbmRvYmoKMTQgMCBvYmoKPDwKL0xlbmd0aCA3MDgKL0ZpbHRlciAvRmxhdGVEZWNvZGUKPj4Kc3RyZWFtCnicVVJNTBNBFJ6Zlm3EAE673VYo0HbbbSlt+dnuttVSihQoBKHE0NISqKSAVUAIQoLBqBePkhg8mHgyHjiZEE4eOXiqRxN/YogHYySeiIkYTbr4ttREJ/lm5tt973vve5m11fU5pEMxRFB7YW5mFp2uOwC5AB8qfA/gWFzO/+VvAZalmY2VCv8OsK6szlU41sOmu7Z4e77CzyNEnIWltY1TronCZkYYdqJ55906E8rVRX4gjeZQ/Xtw/81X9fyc34+UthVt9ZG2FSgDPaoZAK1P+YjQWXNp+/dB9dFp1j+LqZxuwD3AF0jKA3bVLsoaDKZQfxMi8X+ZWoRf4pMHu9qHUGwaUn6RItLAgJBIbdRpo7Zp/FR5giXlNSmWZJHMQhwMheQgrg6BTdzJcUaWYXQGXsNTnpp4KnbKUkAQsJC5FfL0OF5df7GV3Z9fSCedw6Toy8bDw74aZR+7lA+kOKL87OttC9WDbPDkmGBiRybUXJE12jgOxIJGhuHtgiAFZNlkZ3Q2VdzRl5eURySTFEc9Lcng8HrPxYX+7kEdppYwsUeWBqXJnEF/WU+jm5mJu/HEWMIfd4F78eQY70EVD0JOtqzrkspV1JZdfgJFxE7OpBME3s4YWY4zNRG8F7h6KTTWbJ1s641wYSkw5F7Odow7m+xDLjlc3yWKCeFxoN+hP5c819DqYK1WlnWHvPEhA+2vZfkmrrmRNbjDUH0UPBqJGTwiTHkXX0vKxqhIWbUHWaLEmKjRUjnGx9LezIWuSF8jMZcOBywO2pKK45yyE4v50lFsArFxeFgpsOIqj8sIww+Umy5f1Fv5KprEKFEtibJMnk1m01JopDdtbhfkaMPEQE0V7YhYo1cMVQ1TxD5VUJ7jTHcsm1Le44wnbPGlupVvxDxoceq9fr+XTKBpchPNkAAK4k9IJDfQqGYHjf8Be5ij3wplbmRzdHJlYW0KZW5kb2JqCnhyZWYKMCAxOAowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDE2NTcgMDAwMDAgbiAKMDAwMDAwMTcxNCAwMDAwMCBuIAowMDAwMDAxNTk1IDAwMDAwIG4gCjAwMDAwMDE1NzQgMDAwMDAgbiAKMDAwMDAwMDI5MiAwMDAwMCBuIAowMDAwMDAwMTc1IDAwMDAwIG4gCjAwMDAwMDAwNjUgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAxNDI0IDAwMDAwIG4gCjAwMDAwMDA1NzMgMDAwMDAgbiAKMDAwMDAwMDQ4NSAwMDAwMCBuIAowMDAwMDAwNTExIDAwMDAwIG4gCjAwMDAwMDA1MzcgMDAwMDAgbiAKMDAwMDAwMTc2MSAwMDAwMCBuIAowMDAwMDAwNjQ5IDAwMDAwIG4gCjAwMDAwMDA4NjYgMDAwMDAgbiAKMDAwMDAwMTExMSAwMDAwMCBuIAp0cmFpbGVyCjw8Ci9TaXplIDE4Ci9Sb290IDMgMCBSCi9JbmZvIDEwIDAgUgovSUQgWzwzNzQzNmM4MWNlZDczZGZiNDM0NGVjMjE3NzBhZDk3Mz4gPDM3NDM2YzgxY2VkNzNkZmI0MzQ0ZWMyMTc3MGFkOTczPl0KPj4Kc3RhcnR4cmVmCjI1NDIKJSVFT0YK';
  const resumeFile = base64toFile(resumePdf, 'resume.pdf', 'application/pdf');

  // License
  const licensePdf = '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NTM1IGYgCjAwMDAwMDE3MDAgMDAwMDAgbiAKMDAwMDAwMTc1NyAwMDAwMCBuIAowMDAwMDAxNjM4IDAwMDAwIG4gCjAwMDAwMDE2MTcgMDAwMDAgbiAKMDAwMDAwMDI5MiAwMDAwMCBuIAowMDAwMDAwMTc1IDAwMDAwIG4gCjAwMDAwMDAwNjUgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAxNDY3IDAwMDAwIG4gCjAwMDAwMDA1OTEgMDAwMDAgbiAKMDAwMDAwMDUwMyAwMDAwMCBuIAowMDAwMDAwNTI5IDAwMDAwIG4gCjAwMDAwMDA1NTUgMDAwMDAgbiAKMDAwMDAwMTgwNCAwMDAwMCBuIAowMDAwMDAwNjY3IDAwMDAwIG4gCjAwMDAwMDA4ODQgMDAwMDAgbiAKMDAwMDAwMTE0NSAwMDAwMCBuIAp0cmFpbGVyCjw8Ci9TaXplIDE4Ci9Sb290IDMgMCBSCi9JbmZvIDEwIDAgUgovSUQgWzw0MjA3NmU3YzEzNTE0ZDU5YTEzYTE5OWFmZWY4ZGJkMj4gPDQyMDc2ZTdjMTM1MTRkNTlhMTNhMTk5YWZlZjhkYmQyPl0KPj4Kc3RhcnR4cmVmCjI2NzcKJSVFT0YK';
  const licenseFile = base64toFile(licensePdf, 'license.pdf', 'application/pdf');

  // Gov ID
  const govIdImage = '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';
  const govIdFile = base64toFile(govIdImage, 'gov-id.png', "image/png");

  onboardingForm.value.setValues({
    resume: resumeFile,
    licenseFiles: [
      {
        state: 'MD',
        licenseNumber: '123456789',
        expirationDate: '2029-01-02',
        license: licenseFile,
      }
    ],
    govId: govIdFile,
  })
}
</script>

<template>
  <NuxtLink to="/" class="flex p-2">
    <ChevronLeft />
    Back
  </NuxtLink>
  <Form
    v-slot="{ meta, values, validate }"
    as=""
    ref="onboardingForm"
    keep-values
    :initial-values="initialData"
    :validation-schema="toTypedSchema(formSchema[stepIndex - 1])"
  >
    <Stepper v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep, goTo }" v-model="stepIndex" class="block w-full">
      <form
        class="p-4"
        @submit="
          (e) => {
            e.preventDefault();

            validate();

            if (stepIndex === steps.length && meta.valid) {
              values.resume = resumeRef; //TODO: Why??
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
            <p class="font-semibold p-2">Personal Info</p>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <FormField v-slot="{ componentField }" name="firstName">
                <FormItem>
                  <FormLabel>First name</FormLabel>
                  <FormControl>
                    <Input v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <FormField v-slot="{ componentField }" name="middleName">
                <FormItem>
                  <FormLabel>Middle name</FormLabel>
                  <FormControl>
                    <Input v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <FormField v-slot="{ componentField }" name="lastName">
                <FormItem>
                  <FormLabel>Last name</FormLabel>
                  <FormControl>
                    <Input v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
            <div class="grid grid-cols-4 lg:grid-cols-1 gap-4">
              <div class="col-span-3 lg:col-span-1">
                <FormField v-slot="{ componentField }" name="gender">
                  <FormItem>
                    <FormLabel>Gender</FormLabel>
                    <Select v-bind="componentField">
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select a gender" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectGroup>
                          <SelectItem value="Male"> Male </SelectItem>
                          <SelectItem value="Female"> Female </SelectItem>
                        </SelectGroup>
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-1 lg:col-span-1">
                <FormField v-slot="{ handleChange, value }" name="dob">
                  <FormItem>
                    <FormLabel>Date of birth</FormLabel>
                    <FormControl>
                      <DatePicker2
                        :key="value"
                        :defaultValue="value ? new CalendarDate(dayjs(value).year(), dayjs(value).month() + 1, dayjs(value).date()) : undefined"
                        :maxValue="maxDateDob"
                        @update:model-value="
                          (v) => {
                            handleChange(v.toDate(getLocalTimeZone()).toISOString().split('T')[0]);
                          }
                        "
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-1 lg:col-span-1">
                <FormField v-slot="{ componentField }" name="citizenshipStatus">
                  <FormItem>
                    <FormLabel>Citizenship status</FormLabel>
                    <Select v-bind="componentField">
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select a status" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectGroup>
                          <SelectItem value="US Citizen"> US Citizen </SelectItem>
                          <SelectItem value="Green Card"> Green Card </SelectItem>
                          <SelectItem value="Other"> Other </SelectItem>
                        </SelectGroup>
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-1 lg:col-span-1">
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
                          <SelectItem value="Anesthesiology"> Anesthesiology </SelectItem>
                          <SelectItem value="CRNA"> Certified Registered Nurse Anesthetist (CRNA) </SelectItem>
                          <SelectItem value="EMS"> Emergency Medicine Specialists </SelectItem>
                          <SelectItem value="RN"> Registered Nurse (RN) </SelectItem>
                        </SelectGroup>
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-1">
                <FormField v-slot="{ componentField }" name="npi">
                  <FormItem>
                    <FormLabel>NPI</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-1">
                <FormField v-slot="{ componentField }" name="location">
                  <FormItem>
                    <FormLabel>Location</FormLabel>
                    <FormControl>
                      <Select v-bind="componentField">
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Select a state" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectGroup>
                            <SelectItem v-for="s in states" :value="s.id"> {{ s.label }} </SelectItem>
                          </SelectGroup>
                        </SelectContent>
                      </Select>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
            </div>
            <hr></hr>
            <p class="font-semibold p-2">Home Address</p>
            <div>
              <div class="grid grid-cols-1 gap-4">
                <FormField v-slot="{ componentField }" name="homeAddress.addressLine1">
                  <FormItem>
                    <FormLabel>Address Line 1</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <FormField v-slot="{ componentField }" name="homeAddress.addressLine2">
                  <FormItem>
                    <FormLabel>Address Line 2</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
            </div>
            <div>
              <div class="grid grid-cols-1 lg:grid-cols-3 gap-4">
                <FormField v-slot="{ componentField }" name="homeAddress.city">
                  <FormItem>
                    <FormLabel>City</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <FormField v-slot="{ componentField }" name="homeAddress.state">
                  <FormItem>
                    <FormLabel>State</FormLabel>
                    <FormControl>
                      <Select v-bind="componentField">
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Select a state" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectGroup>
                            <SelectItem v-for="s in states" :value="s.id"> {{ s.label }} </SelectItem>
                          </SelectGroup>
                        </SelectContent>
                      </Select>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <FormField v-slot="{ componentField }" name="homeAddress.zip">
                  <FormItem>
                    <FormLabel>Zip Code</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
            </div>
            <hr></hr>
            <p class="font-semibold p-2">Mailing Address</p>
            <div>
              <div class="grid grid-cols-1 gap-4">
                <FormField v-slot="{ componentField }" name="mailingAddress.addressLine1">
                  <FormItem>
                    <FormLabel>Address Line 1</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <FormField v-slot="{ componentField }" name="mailingAddress.addressLine2">
                  <FormItem>
                    <FormLabel>Address Line 2</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
            </div>
            <div>
              <div class="grid grid-cols-1 lg:grid-cols-3 gap-4">
                <FormField v-slot="{ componentField }" name="mailingAddress.city">
                  <FormItem>
                    <FormLabel>City</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <FormField v-slot="{ componentField }" name="mailingAddress.state">
                  <FormItem>
                    <FormLabel>State</FormLabel>
                    <FormControl>
                      <Select v-bind="componentField">
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Select a state" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectGroup>
                            <SelectItem v-for="s in states" :value="s.id"> {{ s.label }} </SelectItem>
                          </SelectGroup>
                        </SelectContent>
                      </Select>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <FormField v-slot="{ componentField }" name="mailingAddress.zip">
                  <FormItem>
                    <FormLabel>Zip Code</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
            </div>
            <hr></hr>
            <p class="font-semibold p-2">Scheduling</p>
            <FormField v-slot="{ componentField }" name="employmentType">
              <FormItem>
                <FormLabel>Employment type</FormLabel>
                <Select v-bind="componentField">
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select an employment type" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem value="Short Term"> Short Term </SelectItem>
                      <SelectItem value="Long Term"> Long Term </SelectItem>
                      <SelectItem value="Short or Long Term"> Short or Long Term </SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            </FormField>
            <FormField name="schedulePreferences">
              <FormItem>
                <FormLabel>Schedule preferences</FormLabel>
                <FormField
                  v-for="item in schedulePreferences"
                  v-slot="{ value, handleChange }"
                  :key="item.id"
                  type="checkbox"
                  :value="item.id"
                  :unchecked-value="false"
                  name="schedulePreferences"
                >
                  <FormItem class="flex flex-row items-start space-y-0">
                    <FormControl>
                      <Checkbox :model-value="value.includes(item.id)" @update:model-value="handleChange" />
                    </FormControl>
                    <FormLabel class="font-normal">
                      {{ item.label }}
                    </FormLabel>
                  </FormItem>
                </FormField>
                <FormMessage />
              </FormItem>
            </FormField>
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
            <hr />
            <FormFieldArray name="licenseFiles" v-slot="{ fields, push, remove }">
              <fieldset class="InputGroup" v-for="(field, idx) in fields" :key="field.key">
                <div class="grid grid-cols-1 sm:grid-cols-5 gap-4">
                  <FormField v-slot="{ componentField }" :name="`licenseFiles[${idx}].state`">
                    <FormItem>
                      <FormLabel>State</FormLabel>
                      <Select v-bind="componentField">
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="Select a state" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectGroup>
                            <SelectItem v-for="s in states" :value="s.id"> {{ s.label }} </SelectItem>
                          </SelectGroup>
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                  <FormField v-slot="{ componentField }" :name="`licenseFiles[${idx}].licenseNumber`">
                    <FormItem>
                      <FormLabel>License #</FormLabel>
                      <FormControl>
                        <Input v-bind="componentField" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                  <FormField v-slot="{ handleChange, value }" :name="`licenseFiles[${idx}].expirationDate`">
                    <FormItem>
                      <FormLabel>Expiration Date</FormLabel>
                      <FormControl>
                        <DatePicker2
                        :key="value"
                        :defaultValue="value ? new CalendarDate(dayjs(value).year(), dayjs(value).month() + 1, dayjs(value).date()) : undefined"
                        :maxValue="maxDateDob"
                        @update:model-value="
                          (v) => {
                            handleChange(v.toDate(getLocalTimeZone()).toISOString().split('T')[0]);
                          }
                        "
                      />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                  <div class="col-span-1 sm:col-span-2 flex">
                    <FormField v-slot="{ componentField }" :name="`licenseFiles[${idx}].license`">
                      <FormItem class="w-full">
                        <FormLabel>License (PDF)</FormLabel>
                        <FormControl>
                          <input
                            type="file"
                            v-bind="componentField"
                            @change="handleLicenseChange"
                            class="relative m-0 block w-full min-w-0 flex-auto cursor-pointer rounded-md border border-solid border-secondary-500 bg-transparent bg-clip-padding px-3 py-[0.32rem] text-base font-normal text-surface transition duration-300 ease-in-out file:-mx-3 file:-my-[0.32rem] file:me-3 file:cursor-pointer file:overflow-hidden file:rounded-none file:border-0 file:border-e file:border-solid file:border-inherit file:bg-transparent file:px-3 file:py-[0.32rem] file:text-surface focus:border-primary focus:text-gray-700 focus:shadow-inset focus:outline-none dark:border-white/70 dark:text-white file:dark:text-white"
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    </FormField>
                    <div v-if="idx != 0" class="mt-[21px] ml-3">
                      <Button variant="outline" size="icon" @click="remove(idx)">
                        <X class="w-4 h-4" />
                      </Button>
                    </div>
                  </div>
                </div>
              </fieldset>
              <Button type="button" variant="outline" @click="push({ state: '' })"> Add State + </Button>
            </FormFieldArray>
            <hr />
            <FormField v-slot="{ componentField }" name="govId">
              <FormItem>
                <FormLabel>Government ID (PDF / PNG / JPG)</FormLabel>
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
            <Button variant="link" @click="loadCredentialsTestData">Load Test Data</Button>
          </template>
          <template v-if="stepIndex === 3">
            <FormFieldArray name="educationDetails" v-slot="{ fields, push, remove }">
              <fieldset class="InputGroup" v-for="(field, idx) in fields" :key="field.key">
                <div class="grid grid-cols-1 sm:grid-cols-5 gap-4">
                  <div class="col-span-1 sm:col-span-3">
                    <FormField v-slot="{ componentField }" :name="`educationDetails[${idx}].schoolName`">
                      <FormItem>
                        <FormLabel>School Name</FormLabel>
                        <Input v-bind="componentField" />
                        <FormMessage />
                      </FormItem>
                    </FormField>
                  </div>
                  <FormField v-slot="{ handleChange, value }" :name="`educationDetails[${idx}].graduationDate`">
                    <FormItem>
                      <FormLabel>Graduation Date</FormLabel>
                      <FormControl>
                        <DatePicker2
                        :key="value"
                        :defaultValue="value ? new CalendarDate(dayjs(value).year(), dayjs(value).month() + 1, dayjs(value).date()) : undefined"
                        @update:model-value="
                          (v) => {
                            handleChange(v.toDate(getLocalTimeZone()).toISOString().split('T')[0]);
                          }
                        "
                      />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                  <div class="col-span-1 flex">
                    <FormField v-slot="{ componentField }" :name="`educationDetails[${idx}].degree`">
                      <FormItem class="flex-auto">
                        <FormLabel>Degree</FormLabel>
                        <FormControl>
                          <Select v-bind="componentField">
                          <FormControl class="w-full">
                            <SelectTrigger>
                              <SelectValue placeholder="Select a degree type" />
                            </SelectTrigger>
                          </FormControl>
                          <SelectContent>
                            <SelectGroup>
                              <SelectItem v-for="dt in degreeTypes" :value="dt.id"> {{ dt.label }} </SelectItem>
                            </SelectGroup>
                          </SelectContent>
                        </Select>
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    </FormField>
                    <div v-if="idx != 0" class="mt-[21px] ml-3">
                      <Button variant="outline" size="icon" @click="remove(idx)">
                        <X class="w-4 h-4" />
                      </Button>
                    </div>
                  </div>
                </div>
              </fieldset>
              <Button type="button" variant="outline" @click="push({ state: '' })"> Add Education + </Button>
            </FormFieldArray>
            <Button variant="link" @click="loadEducationTestData">Load Test Data</Button>
          </template>
          <template v-if="stepIndex === 4">
            <FormFieldArray name="employmentHistory" v-slot="{ fields, push, remove }">
              <fieldset class="InputGroup" v-for="(field, idx) in fields" :key="field.key">
                <div class="grid grid-cols-1 sm:grid-cols-4 gap-4">
                  <div class="col-span-1 sm:col-span-2">
                    <FormField v-slot="{ componentField }" :name="`employmentHistory[${idx}].employerName`">
                      <FormItem>
                        <FormLabel>Employer Name</FormLabel>
                        <Input v-bind="componentField" />
                        <FormMessage />
                      </FormItem>
                    </FormField>
                  </div>
                  <FormField v-slot="{ handleChange, value }" :name="`employmentHistory[${idx}].startDate`">
                    <FormItem>
                      <FormLabel>Start Date</FormLabel>
                      <FormControl>
                        <DatePicker2
                        :key="value"
                        :defaultValue="value ? new CalendarDate(dayjs(value).year(), dayjs(value).month() + 1, dayjs(value).date()) : undefined"
                        @update:model-value="
                          (v) => {
                            handleChange(v.toDate(getLocalTimeZone()).toISOString().split('T')[0]);
                          }
                        "
                      />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                  <FormField v-slot="{ handleChange, value }" :name="`employmentHistory[${idx}].endDate`">
                    <FormItem>
                      <FormLabel>End Date</FormLabel>
                      <FormControl>
                        <DatePicker2
                        :key="value"
                        :defaultValue="value ? new CalendarDate(dayjs(value).year(), dayjs(value).month() + 1, dayjs(value).date()) : undefined"
                        @update:model-value="
                          (v) => {
                            handleChange(v.toDate(getLocalTimeZone()).toISOString().split('T')[0]);
                          }
                        "
                      />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                  <div class="col-span-1 sm:col-span-4">
                    <FormField v-slot="{ componentField }" :name="`employmentHistory[${idx}].positionDetails`">
                      <FormItem>
                        <FormLabel>Description</FormLabel>
                        <FormControl>
                          <Textarea v-bind="componentField" class="h-32" />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    </FormField>
                  </div>
                  <div class="col-span-1 sm:col-span-2">
                    <FormField v-slot="{ componentField }" :name="`employmentHistory[${idx}].supervisorName`">
                      <FormItem>
                        <FormLabel>Supervisor Name</FormLabel>
                        <FormControl>
                          <Input v-bind="componentField" />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    </FormField>
                  </div>
                  <div class="col-span-1 sm:col-span-2 flex">
                    <FormField v-slot="{ componentField }" :name="`employmentHistory[${idx}].supervisorContact`">
                      <FormItem class="w-full">
                        <FormLabel>Supervisor Contact</FormLabel>
                        <FormControl>
                          <Input v-bind="componentField" />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    </FormField>
                    <div v-if="idx != 0" class="mt-[21px] ml-3">
                      <Button variant="outline" size="icon" @click="remove(idx)">
                        <X class="w-4 h-4" />
                      </Button>
                    </div>
                  </div>
                </div>
              </fieldset>
              <Button type="button" variant="outline" @click="push({ state: '' })"> Add Work History + </Button>
            </FormFieldArray>
            <Button variant="link" @click="loadWorkHistoryTestData">Load Test Data</Button>
          </template>
          <template v-if="stepIndex === 5">
            <OnboardingReview :values="values" @goToStep="(step) => stepIndex = step" />
            <FormField v-slot="{ value, handleChange }" type="checkbox" name="attestationOne">
              <FormItem class="flex flex-row items-start gap-x-3 space-y-0 rounded-md border p-4 shadow">
                <FormControl>
                  <Checkbox :model-value="value" @update:model-value="handleChange" />
                </FormControl>
                <div class="space-y-1 leading-none">
                  <FormLabel>I attest that the information provided is accurate and truthful.</FormLabel>
                  <FormMessage />
                </div>
              </FormItem>
            </FormField>
            <FormField v-slot="{ value, handleChange }" type="checkbox" name="attestationTwo">
              <FormItem class="flex flex-row items-start gap-x-3 space-y-0 rounded-md border p-4 shadow">
                <FormControl>
                  <Checkbox :model-value="value" @update:model-value="handleChange" />
                </FormControl>
                <div class="space-y-1 leading-none">
                  <FormLabel>I authorize ShiftStat to share my credential packet with facilities I apply to.</FormLabel>
                  <FormMessage />
                </div>
              </FormItem>
            </FormField>
            <div class="grid grid-cols-1 sm:grid-cols-4 gap-4">
              <div class="col-span-1 sm:col-span-3">
                <FormField v-slot="{ componentField }" name="signature">
                  <FormItem>
                    <FormLabel>Signiture</FormLabel>
                    <FormDescription>Please type your full name.</FormDescription>
                    <FormControl>
                      <Input v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
              <div class="col-span-1 sm:col-span-1">
                <FormField v-slot="{ componentField }" name="signatureDate">
                  <FormItem>
                    <FormLabel>Signiture Date</FormLabel>
                    <FormDescription>-</FormDescription>
                    <FormControl>
                      <Input v-bind="componentField" disabled />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>
            </div>
          </template>
        </div>
        <div class="flex items-center justify-between mt-4">
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
            <Button v-if="stepIndex === steps.length" size="sm" type="submit" :disabled="saving || submitted">
              <Loader2 v-if="saving" class="w-4 h-4 animate-spin" />
              Submit
            </Button>
          </div>
        </div>
      </form>
    </Stepper>
  </Form>
</template>
