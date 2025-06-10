<template>
  <Button variant="outline" @click="download(params.data.resumeId)" :disabled="downloading">
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
    colDef: { context },
  },
} = props;

const downloading = ref(false);
const download = (key) => {
  downloading.value = true;
  $fetch(`/api/onboarding/resume/${key}`, {
    headers: {
      Accept: "application/pdf",
    },
  }).then((blob) => {
    FileSaver.saveAs(blob, "resume.pdf");
  });
  downloading.value = false;
}
</script>
