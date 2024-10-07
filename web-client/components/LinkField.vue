<template>
  <IButton link color="dark" :to="`${context}/${params.data.id}`">
    {{ params.value }}
  </IButton>
</template>
<script setup>
const props = defineProps(["params"]);
const {
  params: {
    colDef: { context },
  },
} = props;
console.log(context);
</script>
