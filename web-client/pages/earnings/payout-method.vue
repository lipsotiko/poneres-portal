<template>
  <IContainer>
    <PageTitle title="Get paid by Property Pal" back-to="/earnings" />
    <img src="/stripe-label.svg" />
    <i class="fa-solid fa-arrow-right"></i>
    <IButton color="primary" @click="goToPayoutSetup">
      Go through setup
    </IButton>
  </IContainer>
</template>
<script setup>
const goToPayoutSetup = async () => {
  const accountLink = await getPaymentAccountlink();
  navigateTo(accountLink.url, { external: true });
};
</script>
<style scoped>
.fa-arrow-right {
  margin: 22px;
}
</style>
