<template>
  <Button variant="outline" size="sm" @click="download()" :disabled="downloading">
    <Loader2 v-if="downloading" class="w-4 h-4 animate-spin" />
    Download
  </Button>
</template>
<script setup>
import FileSaver from "file-saver";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-vue-next";

const props = defineProps(["params"]);
const {
  params: {
    data: {
      resumeId,
      licenseId
    },
    colDef: { context },
  },
} = props;

const downloading = ref(false);
const download = () => {
  downloading.value = true;

  const key = context === 'resume' ? resumeId : licenseId;
  const fileName = context === 'resume' ? 'resume.pdf' : 'license.pdf';

  $fetch(`/api/storage/download/${key}`, {
    headers: {
      Accept: "application/pdf",
    },
  }).then((blob) => {
    FileSaver.saveAs(blob, fileName);
  });
  downloading.value = false;
}
</script>
