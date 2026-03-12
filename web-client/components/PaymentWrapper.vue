<template>
  <ClientOnly>
    <PaymentForm v-if="!pending" :client-secret="data.clientSecret" />
    <template #placeholder>
      <div>Loading payment form...</div>
    </template>
  </ClientOnly>
</template>
<script setup>
const { pending, data } = await useAsyncData("payment", () => $fetch("/api/payments/create-payment-intent"));
</script>
