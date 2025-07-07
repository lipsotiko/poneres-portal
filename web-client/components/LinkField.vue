<template>
  <Button variant="link" @click="navigateTo(context + '/' + id)">
    {{ value }}
  </Button>
</template>
<script setup>
import { Button } from "@/components/ui/button";

const props = defineProps(["params"]);
const {
  params: {
    colDef: { context },
    data: { id },
    value,
  },
} = props;
</script>
