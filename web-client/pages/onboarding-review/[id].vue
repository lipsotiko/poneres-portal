<template>
  <DefaultLayoutWrapper>
    <template #breadcrumbs>
      <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbLink href="/"> Home </BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbLink href="/onboarding-review">Onboarding Review</BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbPage>{{ onboardingId }}</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <Card class="p-4">
    <h3>{{ data.firstName }} {{ data.middleName }} {{ data.lastName }}</h3>
    <hr />
    <table>
      <tr>
        <td class="w-28 font-medium">DOB</td>
        <td>{{ data.dob }}</td>
      </tr>
      <tr>
        <td class="w-28 font-medium">NPI</td>
        <td>{{ data.npi }}</td>
      </tr>
      <tr>
        <td class="w-28 font-medium">Location</td>
        <td>{{ data.location }}</td>
      </tr>
      <tr>
        <td class="w-28 font-medium">Specialty</td>
        <td>{{ data.specialty }}</td>
      </tr>
      <tr>
        <td class="w-28 font-medium">Employment</td>
        <td>{{ data.employmentType }}</td>
      </tr>
    </table>
    <div class="mt-4">
      <div>
        <label class="font-medium"> Are there any procedures/cases you are uncomfortable seeing/doing? </label>
        <p class="pl-2">{{ data.uncomfortableProcedures }}</p>
      </div>
      <div>
        <label class="font-medium"> Have you ever done locums before? If so, when and where? </label>
        <p class="pl-2">{{ data.haveYouDoneLocumsBefore }}</p>
      </div>
      <div>
        <label class="font-medium"> Do you have any active certifications? (i.e. BLS, ACLS, NRP, etc.) </label>
        <p class="pl-2">{{ data.activeCertifications }}</p>
      </div>
      <div>
        <label class="font-medium"> Any dropped, pending or settled malpractice cases? </label>
        <p class="pl-2">{{ data.malpractice }}</p>
      </div>
      <hr />
      <div>
        <ul>
          <li>
            <Button variant="link" @click="downloadFile(data.resumeId)">Resume</Button>
          </li>
          <li v-for="f in data.licenseFiles">
            <Button variant="link" @click="downloadFile(f.licenseId)">{{ f.state }} License</Button>
          </li>
        </ul>
      </div>
    </div>
  </Card>
</template>
<script setup>
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
const {
  params: { id: onboardingId },
} = useRoute();

const { pending, data, refresh } = await useAsyncData("onboarding", () => $fetch(`/api/onboarding/${onboardingId}`), {
  server: false,
});

const downloadFile = (key) => {
  navigateTo(`/api/storage/download/${key}`, {
    external: true,
    open: {
      target: "_blank",
    },
  });
};
</script>
