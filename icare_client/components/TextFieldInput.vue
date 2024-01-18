<template>
  <IFormGroup>
    <IFormLabel>{{ label }}</IFormLabel>
    <IInput
      v-bind="$attrs"
      :name="name"
      :placeholder="label"
      :maxLength="maxLength"
      :disabled="disabled"
      :type="type ? type : 'text'"
    />
  </IFormGroup>
</template>
<script>
export default {
  props: ["name", "label", "maxLength", "disabled", "type"],
};
</script>
