<template>
  <IContainer>
    <PageTitle title="My Earnings" back-to="/" />
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
  </IContainer>
</template>
