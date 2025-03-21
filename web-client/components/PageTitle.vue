<template>
  <div class="top">
    <div class="_display:flex">
      <a class="link-no-decoration" @click="navigateTo(backTo)">
        <i class="fa-solid fa-chevron-left"></i>
      </a>
      <p class="lead">{{ title }}</p>
    </div>
    <slot></slot>
  </div>
</template>
<script setup>
const props = defineProps(["title", "backTo"]);
</script>
