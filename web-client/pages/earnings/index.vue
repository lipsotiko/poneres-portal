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
            <BreadcrumbPage>Earnings</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <Button variant="link" @click="() => navigateTo('/earnings/payout-method')">
    <i class="fa-solid fa-building-columns"></i>
    Payout method
  </Button>
  <Button variant="link" @click="() => navigateTo('/earnings/payout-history')">
    <i class="fa-solid fa-clock-rotate-left"></i>
    Payout history
  </Button>
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
</script>
