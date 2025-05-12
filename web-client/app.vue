<template>
  <NuxtLayout>
    <NuxtPage />
  </NuxtLayout>
</template>
<script setup lang="ts">
import "ag-grid-community/styles/ag-grid.css"; // Mandatory CSS required by the Data Grid

const auth = useState("auth");

const { data } = await useAsyncData("user-profile", () => $fetch("/api/user/profile"), {
  server: false,
});

auth.value = data;

// await callOnce(async () => {
// }, { mode: 'navigation' });

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

.row {
  padding-top: 8px;
  padding-bottom: 8px;
}

.link-no-decoration {
  text-decoration: none;
  color: black;
  margin: 2px 8px;
}

.link-no-decoration:hover {
  cursor: pointer;
  color: lightgrey;
}
</style>
