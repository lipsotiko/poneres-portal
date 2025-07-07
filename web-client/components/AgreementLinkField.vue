<template>
  <Button variant="link" @click="() => navigateTo(getAgreementPath(type) + id)">
    {{ value }}
  </Button>
</template>
<script setup>
import { Button } from "@/components/ui/button";

const props = defineProps(["params"]);
const {
  params: {
    data: { id, type },
    value,
  },
} = props;
</script>
