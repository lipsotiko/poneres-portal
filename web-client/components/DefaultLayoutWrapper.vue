<template>
  <div class="flex">
    <SidebarTrigger class="mr-4" />
    <div class="mt-1">
      <slot name="breadcrumbs" />
    </div>
  </div>
</template>
<script setup>
import { SidebarTrigger } from "@/components/ui/sidebar";
</script>
