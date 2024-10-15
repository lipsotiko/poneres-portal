<template>
  <div v-if="ssdId == null">
    <IBadge>Draft</IBadge>
    <IButton size="sm" color="primary" @click="send(id)" :loading="loading">
      Send for signing
    </IButton>
  </div>
  <span v-else>
    <IBadge v-if="loading" color="warning">Loading...</IBadge>
    <IBadge v-else>
      {{ status }}
    </IBadge>
    <IButton
      v-if="status === 'Completed'"
      size="sm"
      color="secondary"
      @click="handleGetCompletedFile()"
      :loading="getFileLoading"
    >
      Download</IButton
    >
  </span>
</template>
<script setup>
const props = defineProps(["params"]);
const loading = ref(false);
const getFileLoading = ref(false);
const status = ref("-");
const {
  params: {
    colDef: {
      context: { onSend },
    },
    data: { id, ssdId },
  },
} = props;

const send = async (id) => {
  loading.value = true;
  await sendForSigning(id);
  onSend();
};

onMounted(async () => {
  loading.value = true;
  status.value = await getSignatureStatus(id);
  loading.value = false;
});

const handleGetCompletedFile = async () => {
  getFileLoading.value = true;
  const fileUrl = await getFileUrl(id);
  navigateTo(fileUrl, {
    external: true,
    open: {
      target: "_blank",
    },
  });
  getFileLoading.value = false;
};
</script>
<style>
.badge {
  width: 100px;
  margin-right: 12px;
}

.button {
  height: 22px;
}
</style>
