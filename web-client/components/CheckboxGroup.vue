<template>
  <div v-for="o in options">
    <Checkbox
    :model-value="model[o.value]"
    @update:model-value="(e) => handleChange(e, o.value)" />
    <label>{{ o.label }}</label>
  </div>
</template>
<script setup>
import { Checkbox } from "@/components/ui/checkbox";
const { options } = defineProps(["options"]);

const model = defineModel({ default: {}});
const emit = defineEmits(["update:modelValue"]);

const handleChange = (checked, val) => {
  if (checked) {
    model.value[val] = true;
  } else {
    model.value[val] = false;
  }
  emit('update:modelValue', model.value);
}
</script>
