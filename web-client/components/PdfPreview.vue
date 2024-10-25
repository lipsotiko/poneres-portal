<template>
  <ISidebar v-model="sideBarOpen" class="sidebar" collapse-position="fixed" :collapse="true" size="lg">
    <ILoader v-if="loadingPreview" />
    <IButton v-else size="sm" @click="() => (sideBarOpen = false)">Close</IButton>
    <iframe ref="pdfPreview" class="preview-pdf"> </iframe>
  </ISidebar>
</template>
<script setup>
import { inject } from "vue";
const sideBarOpen = inject("sideBarOpen");
const pdfPreview = ref();
const loadingPreview = ref(false);
const { type, metadata, recipients } = defineProps(["type", "metadata", "recipients"]);

onUpdated(async () => {
  if (sideBarOpen.value) {
    pdfPreview.value.src = "";
    loadingPreview.value = true;
    await previewAgreement(type, { metadata, recipients }).then((blob) => {
      var file = window.URL.createObjectURL(blob);
      pdfPreview.value.src = file;
    });
    loadingPreview.value = false;
  }
});
</script>
<style scoped>
.preview-pdf {
  height: 100%;
}
.sidebar {
  --sidebar--width: 1100px;
}
</style>
