<template>
  <Checkbox2 v-for="o in options" :label="o.label" :value="o.value" />
</template>
<script setup>
const { options } = defineProps(["options"]);

// TODO: emit an array of seleted options
</script>
