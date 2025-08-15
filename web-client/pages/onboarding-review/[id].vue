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
      <tbody>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">DOB</td>
          <td>{{ data.dob }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Gender</td>
          <td>{{ data.gender }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">NPI</td>
          <td>{{ data.npi }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Location</td>
          <td>{{ data.location }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Citizenship status</td>
          <td>{{ data.citizenshipStatus }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Specialty</td>
          <td>{{ data.specialty }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Employment</td>
          <td>{{ data.employmentType }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Schedule preferences</td>
          <td>{{ data.schedulePreferences }}</td>
        </tr>
      </tbody>
    </table>
    <div class="mt-4">
      <hr />
      <div class="mt-4">
        <ul>
          <li>
            <Button variant="link" @click="downloadFile(data.resumeFileId)">Resume</Button>
          </li>
          <li v-for="f in data.licenseFiles">
            <Button variant="link" @click="downloadFile(f.licenseFileId)">{{ f.state }} License</Button>
          </li>
          <li>
            <Button variant="link" @click="downloadFile(data.govIdFileId)">Government ID</Button>
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
