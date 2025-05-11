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
  <IButton link color="dark" to="/earnings/payout-method">
    Payout method
    <template #icon>
      <i class="fa-solid fa-building-columns"></i>
    </template>
  </IButton>
  <IButton link color="dark" to="/earnings/payout-history">
    Payout history
    <template #icon>
      <i class="fa-solid fa-clock-rotate-left"></i>
    </template>
  </IButton>
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
</script>
