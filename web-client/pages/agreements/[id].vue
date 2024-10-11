<template>
  <p>{{ agreementId }}</p>
</template>
<script setup>
const {
  params: { id: agreementId },
} = useRoute();
</script>
