<template>
  <IContainer>
    <div class="top">
      <p v-if="isAdmin" class="lead">
        Welcome to your management portal!
      </p>
      <p v-if="isTenant" class="lead">
        Welcome to your tenant portal!
      </p>
    </div>
  </IContainer>
</template>
<script setup>
const { isAdmin, isTenant } = useAuth();
</script>
