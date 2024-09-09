<template>
  <IFormGroup>
    <IFormLabel>{{ label }}</IFormLabel>
    <INumberInput v-bind="$attrs" :name="name" :placeholder="label" />
  </IFormGroup>
</template>
<script>
export default {
  props: ["name", "label"],
};
</script>
