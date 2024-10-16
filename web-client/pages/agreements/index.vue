<template>
  <IToast v-if="modalOpen" v-model="modalOpen" color="success" :duration="2500" dismissible>
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
  </IContainer>
</template>
<script setup>
import "ag-grid-community/styles/ag-theme-quartz.css";
import { AgGridVue } from "ag-grid-vue3";
import { ref } from "vue";

const modalOpen = ref(false);
const modalMessage = ref();
const page = ref(1);
const sort = ref("createdAt,asc");
const colDefs = ref([
  {
    field: "client",
    context: "/agreements/lease_agreement_md",
    cellRenderer: "LinkField",
    width: 500,
  },
  {
    field: "agreementType",
    headerName: "Type",
    width: 300,
  },
  {
    field: "createdAt",
    width: 200,
  },
  {
    field: "ssdId",
    headerName: "Signature",
    context: {
      onSend: () => {
        modalMessage.value = "Document sent for signing!";
        modalOpen.value = true;
        refresh();
      },
      onReminderSent: () => {
        modalMessage.value = "Reminder sent!";
        modalOpen.value = true;
      },
    },
    cellRenderer: "SignatureStatus",
    headerName: "Status / Action",
    width: 288,
  },
]);

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
import { LinkField, SignatureStatus } from "#components";

export default {
  components: {
    LinkField,
    SignatureStatus,
  },
};
</script>
