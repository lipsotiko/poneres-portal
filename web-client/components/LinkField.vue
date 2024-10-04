<template>
  <IButton
    v-if="context === 'UserLink'"
    link
    color="dark"
    :to="`/admin/users/${params.data.id}`"
    >{{ params.value }}</IButton
  >
  <IButton
    v-else-if="context === 'PropertyLink'"
    link
    color="dark"
    :to="`/properties/${params.data.id}`"
    >{{ params.value }}</IButton
  >
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
