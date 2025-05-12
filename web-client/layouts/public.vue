<template>
  <div class="m-4">
    <IToast v-if="errorMessage" color="warning" dismissible>
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
