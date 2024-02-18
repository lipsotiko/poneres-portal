<template>
  <IButton color="primary" size="sm" @click="download(id)" :loading="downloading">
    Download
  </IButton>
</template>
<script>
import FileSaver from "file-saver";
export default {
  props: ["id"],
  data() {
    return {
      downloading: false,
    };
  },
  methods: {
    async download(id) {
      this.downloading = true;
      getPatientApplicationPdfPreview(id).then((blob) => {
        FileSaver.saveAs(blob, "application.pdf");
      });
      this.downloading = false;
    },
  },
};
</script>
