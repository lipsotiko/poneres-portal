<template>
  <div v-if="ssdId == null">
    <IBadge>Draft</IBadge>
    <IButton size="sm" color="primary" @click="send(id)" :loading="loading"> Send for signing </IButton>
  </div>
  <span v-else>
    <IBadge v-if="loading" color="warning">Loading...</IBadge>
    <div v-else>
      <IButton
        v-if="status === 'Completed'"
        size="sm"
        color="secondary"
        @click="handleGetCompletedFile()"
        :loading="getFileLoading"
      >
        Download</IButton
      >
      <IButton
        v-else-if="status !== 'Completed'"
        size="sm"
        color="secondary"
        @click="handleSendReminder()"
        :loading="reminderLoading"
      >
        Send Reminder</IButton
      >
      <IBadge v-else>
        {{ status }}
      </IBadge>
    </div>
  </span>
</template>
<script setup>
const props = defineProps(["params"]);
const loading = ref(false);
const getFileLoading = ref(false);
const reminderLoading = ref(false);
const status = ref("-");
const {
  params: {
    colDef: {
      context: { onSend, onReminderSent },
    },
    data: { id, ssdId },
  },
} = props;

const send = async (id) => {
  loading.value = true;
  onSend(id);
  loading.value = false;
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

const handleSendReminder = async () => {
  reminderLoading.value = true;
  await sendReminder(id);
  onReminderSent();
  reminderLoading.value = false;
};
</script>
<style scoped>
.badge {
  width: 100px;
  margin-right: 12px;
}

.button {
  width: 122px;
  height: 22px;
}
</style>
