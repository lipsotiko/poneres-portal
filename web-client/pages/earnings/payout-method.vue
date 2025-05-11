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
            <BreadcrumbLink href="/earnings">Earnings</BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbPage>Payment Method</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <img src="/stripe-label.svg" />
  <i class="fa-solid fa-arrow-right"></i>
  <IButton color="primary" :loading="loading" @click="goToPayoutSetup"> Go through setup </IButton>
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
const loading = ref(false);
const goToPayoutSetup = async () => {
  loading.value = true;
  const accountLink = await getPaymentAccountlink();
  navigateTo(accountLink.url, { external: true });
};
</script>
<style scoped>
.fa-arrow-right {
  margin: 22px;
}
</style>
