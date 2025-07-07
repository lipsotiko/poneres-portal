<script setup>
import { Checkbox } from "@/components/ui/checkbox";
import { cn } from "@/lib/utils";

const props = defineProps(["label", "class", "name"]);
// const model = defineModel()
const emit = defineEmits(["update:modelValue"]);
</script>
<template>
  <div :class="props.class">
    <Checkbox :id="props.name" :name="props.name" @update:modelValue="(e) => emit('update:modelValue', e)" />
    <label class="m-2">{{ props.label }}</label>
  </div>
</template>
