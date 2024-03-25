<template>
  <IRadioGroup
    v-model="selectedValue"
    :name="name"
    :options="options"
    @update:modelValue="update"
  />
</template>
<script setup>
import { ref, defineProps, onUpdated, onMounted } from "vue";

/**
 * Some applications have fields with check boxes rather than radio fields;
 * this will set one checkbox to true from a set of checkbox fields.
 */

const props = defineProps(["name", "options", "metadata"]);
const selectedValue = ref();

const handleMountAndUpdate = () => {
  for (const o of props.options) {
    if (props.metadata[o.id] === true) {
      selectedValue.value = o.id;
      break;
    }
  }
};

onMounted(() => handleMountAndUpdate());

onUpdated(() => handleMountAndUpdate());

const update = (selected) => {
  const updates = props.options.map(({ id }) => ({
    id,
    selected: id === selected,
  }));

  updates.forEach(({ id, selected }) => (props.metadata[id] = selected));
};
</script>
