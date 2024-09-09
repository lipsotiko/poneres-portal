<template>
  <NuxtLayout>
    <NuxtPage />
  </NuxtLayout>
</template>
<script setup>
useHead({
  script: [
    {
      type: "text/javascript",
      src: "https://kit.fontawesome.com/11a21c475b.js",
      crossorigin: "anonymous",
    },
  ],
});
</script>
<style>
.top {
  margin: 22px;
  display: flex;
  justify-content: space-between;
}
</style>
