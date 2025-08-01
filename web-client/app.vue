<template>
  <Toaster />
  <NuxtLayout>
    <NuxtPage />
  </NuxtLayout>
</template>
<script setup lang="ts">
import { configure } from 'vee-validate';
import { Toaster } from "@/components/ui/sonner";
import "ag-grid-community/styles/ag-grid.css"; // Mandatory CSS required by the Data Grid

configure({
  validateOnChange: false,
  validateOnInput: false,
  validateOnModelUpdate: false,
});
</script>
