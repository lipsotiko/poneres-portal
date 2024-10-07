<template>
  <p>{{ agreementId }}</p>
  <iframe
    id="pdf-preview"
    ref="pdfPreview"
    src="/1r41ai10801601_fong.pdf"
    width="1000px"
    height="1200px"
  ></iframe>
</template>
<script setup>
definePageMeta({
  layout: "public",
});
const {
  params: { id: agreementId },
} = useRoute();
</script>
<style scoped>
.pdf-preview {
  position: absolute;
}
</style>
