<template>
  <div v-if="ssdId == null">
    <Badge variant="secondary" class="mx-2">Draft</Badge>
    <Button class="h-6" @click="send(id)" :disabled="loading">
      <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
      Send for signing
    </Button>
  </div>
  <span v-else>
    <Badge v-if="loading" color="warning">Loading...</Badge>
    <div v-else>
      <Button
        v-if="status === 'Completed'"
        class="h-6"
        variant="secondary"
        @click="handleGetCompletedFile()"
        :disabled="getFileLoading"
      >
        <Loader2 v-if="getFileLoading" class="w-4 h-4 animate-spin" />
        Download
      </Button>
      <Button
        v-else-if="status !== 'Completed'"
        class="h-6 bg-blue-500"
        @click="handleSendReminder()"
        :disabled="reminderLoading"
      >
        <Loader2 v-if="reminderLoading" class="w-4 h-4 animate-spin" />
        Send Reminder
      </Button>
      <Badge v-else>
        {{ status }}
      </Badge>
    </div>
  </span>
</template>
<script setup>
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-vue-next";

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
