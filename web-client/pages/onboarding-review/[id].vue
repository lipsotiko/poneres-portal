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
            <BreadcrumbLink as-child>
              <NuxtLink to="/onboarding-review"> Onboarding Review </NuxtLink>
            </BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbPage>{{ onboardingId }}</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <div class="m-2">
    <OnboardingReview :values="data" hideEdit />
    <div>
      <div class="m-2">
        <label class="font-semibold">Signiture</label>
        <Input v-model="data.signature" disabled />
      </div>
      <div class="m-2">
        <label class="font-semibold">Signiture Date</label>
        <Input v-model="data.signatureDate" disabled />
      </div>
    </div>
  </div>
  
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
import { Input } from "@/components/ui/input";

const {
  params: { id: onboardingId },
} = useRoute();

const { pending, data, refresh } = await useAsyncData(
  "onboardingById",
  () => $fetch(`/api/onboarding/${onboardingId}`),
  {
    server: false,
  },
);
</script>
