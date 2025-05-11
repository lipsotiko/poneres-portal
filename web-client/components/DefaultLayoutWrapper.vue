<template>
  <div class="m-4 flex">
    <SidebarTrigger />
    <div class="mt-1">
      <slot name="breadcrumbs" />
    </div>
  </div>
</template>
<script setup>
import { SidebarTrigger } from "@/components/ui/sidebar";
</script>
