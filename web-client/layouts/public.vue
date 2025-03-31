<template>
  <div>
    <IToast v-if="errorMessage" color="warning" position="bottom" dismissible>
      <p>{{ errorMessage }}</p>
    </IToast>
    <slot />
  </div>
</template>
<script setup>
import { provide } from "vue";
const errorMessage = ref();
provide("errorMessage", errorMessage);
</script>
