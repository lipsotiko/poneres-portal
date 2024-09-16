<template>
  <IFormGroup :disabled="disabled">
    <IFormLabel>{{ label }}</IFormLabel>
    <IInput
      v-bind="$attrs"
      :name="name"
      :placeholder="label"
      :maxLength="maxLength"
      :disabled="disabled"
      :type="type ? type : 'text'"
      :autocomplete="autocomplete"
    >
    </IInput>
  </IFormGroup>
</template>
<script>
export default {
  props: ["name", "label", "maxLength", "disabled", "type", "autocomplete"],
};
</script>
