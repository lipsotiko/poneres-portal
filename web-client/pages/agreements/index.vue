<template>
  <IToast v-if="toastOpen" v-model="toastOpen" color="success" :duration="2500" dismissible>
    <p>{{ modalMessage }}</p>
  </IToast>
  <IContainer>
    <PageTitle title="Agreements" backTo="/">
      <IButton circle color="primary" to="/agreements/new">
        <template #icon>
          <IIcon name="ink-plus" />
        </template>
      </IButton>
    </PageTitle>
    <ag-grid-vue
      :loading="pending"
      :rowData="data?.content"
      :columnDefs="colDefs"
      :gridOptions="gridOptions"
      style="height: 888px"
      class="ag-theme-quartz"
    />
    <IPagination v-model="page" :items-total="data?.totalElements" :items-per-page="data?.size" />
    <IModal v-model="sendAgreementModal">
      <template #header>Send agreement?</template>
      <span v-html="sendAgreementModalMessage"></span>
      <template #footer>
        <div style="display: flex; justify-content: space-between">
          <IButton outline color="dark" @click="sendAgreementModal = false">No, don't</IButton>
          <IButton color="success" :loading="sending" @click="handleSendForSigning">Yes, send it!</IButton>
        </div>
      </template>
    </IModal>
  </IContainer>
</template>
<script setup>
import "ag-grid-community/styles/ag-theme-quartz.css";
import { AgGridVue } from "ag-grid-vue3";
import { ref } from "vue";

const toastOpen = ref(false);
const modalMessage = ref();
const sending = ref(false);
const sendAgreementModal = ref(false);
const sendAgreementModalMessage = ref('Are you sure you want to send this agreement?');
const agreementToSendId = ref();
const page = ref(1);
const sort = ref("createdAt,asc");
const colDefs = ref([
  {
    field: "fileName",
    cellRenderer: "AgreementLinkField",
    width: 800,
  },
  {
    field: "createdAt",
    width: 200,
  },
  {
    field: "ssdId",
    headerName: "Signature",
    context: {
      onSend: async (id) => {
        agreementToSendId.value = id;
        const testMode = await isTestMode();
        if (!testMode) {
          sendAgreementModalMessage.value = 'System is NOT in test mode!<br>Are you sure you want to send this agreement?';
        }
        sendAgreementModal.value = true;
      },
      onReminderSent: () => {
        modalMessage.value = "Reminder sent!";
        toastOpen.value = true;
      }
    },
    cellRenderer: "SignatureStatus",
    headerName: "Status / Action",
    width: 288,
  },
]);

const handleSendForSigning = async () => {
  sending.value = true;
  await sendForSigning(agreementToSendId.value);
  modalMessage.value = "Document sent for signing!";
  toastOpen.value = true;
  sendAgreementModal.value = false;
  sending.value = false;
  refresh();
}

const gridOptions = {
  enableCellTextSelection: true,
  suppressMultiSort: true,
  onSortChanged: ({ columns }) => {
    const i = columns.length - 1;
    const { colId, sort: sortDirection } = columns[i];
    if (sortDirection !== null) {
      sort.value = `${colId},${sortDirection}`;
    }
  },
};

const { pending, data, refresh } = await useAsyncData(
  "agreements",
  () =>
    $fetch("/api/agreements", {
      query: {
        page: page.value - 1,
      },
    }),
  {
    server: false,
    watch: [page, sort],
  },
);
</script>
<style scoped>
.ag-theme-quartz {
  --ag-borders: none;
}

.ag-body-vertical-scroll-viewport {
  overflow-y: hidden;
}
</style>
<script>
import { LinkField, SignatureStatus, AgreementLinkField } from "#components";

export default {
  components: {
    LinkField,
    SignatureStatus,
    AgreementLinkField,
  },
};
</script>
