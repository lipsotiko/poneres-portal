<template>
  <div class="top">
    <div class="_display:flex">
      <a class="link-no-decoration">
        <i class="fa-solid fa-chevron-left" @click="navigateTo(backTo)"></i>
      </a>
      <p class="lead">{{ title }}</p>
    </div>
    <slot></slot>
  </div>
</template>
<script setup>
const props = defineProps(["title", "backTo"]);
</script>
