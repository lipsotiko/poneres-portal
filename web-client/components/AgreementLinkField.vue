<template>
  <IButton link color="dark" :to="getAgreementPath(type) + params.data.id">
    {{ params.value }}
  </IButton>
</template>
<script setup>
const props = defineProps(["params"]);
const {
  params: {
    data: { type },
  },
} = props;
</script>
