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
  <Button @click="goToPayoutSetup" :disabled="loading">
    <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
    Go through setup
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
import { Loader2 } from "lucide-vue-next";

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
