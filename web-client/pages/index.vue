<template>
  <IContainer>
    <div class="top">
      <p v-if="isAdmin" class="lead">
        Welcome to your management portal!
      </p>
      <p v-if="isResident" class="lead">
        Welcome to your resident portal!
      </p>
      <p v-if="isOwner" class="lead">
        Welcome to your owner portal!
      </p>
    </div>
  </IContainer>
</template>
<script setup>
const { isAdmin, isResident, isOwner } = useAuth();
</script>
