<template>
  <IContainer>
    <PageTitle title="Admin panel" backTo="/" />
    <div class="_display:flex _justify-content:end admin-actions">
      <IButton
        outline
        size="sm"
        color="danger"
        :disabled="selectedUserIds.length === 0"
        :loading="deleteing"
        @click="handleDelete()"
        >Delete
      </IButton>
    </div>
    <ag-grid-vue
      :loading="pending"
      :rowData="data?.content"
      :columnDefs="colDefs"
      :gridOptions="gridOptions"
      :rowSelection="rowSelection"
      :getRowId="(params) => String(params.data.id)"
      style="height: 888px"
      class="ag-theme-quartz"
    />
    <IPagination
      v-model="page"
      :items-total="data?.totalElements"
      :items-per-page="data?.size"
    />
  </IContainer>
</template>
<script setup>
import "ag-grid-community/styles/ag-theme-quartz.css";
import { AgGridVue } from "ag-grid-vue3";
const deleteing = ref(false);
const page = ref(1);
const sort = ref("firstName,asc");
const colDefs = ref([
  {
    colId: "firstName",
    headerName: "Name",
    valueGetter: (p) => `${p.data.firstName} ${p.data.lastName}`,
    context: "UserLink",
    cellRenderer: "LinkField",
  },
  { field: "email" },
  { field: "roles" },
  { field: "authProviderId" },
  { field: "paymentProviderId" },
]);

const rowSelection = {
  mode: "multiRow",
};

const selectedUserIds = ref([]);

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
  onSelectionChanged: (e) => {
    selectedUserIds.value = e.api
      .getSelectedNodes()
      .map(({ data: { id } }) => id);
  },
};

const { pending, data, refresh } = await useAsyncData(
  "properties-created-by",
  () =>
    $fetch("/api/user", {
      query: {
        page: page.value - 1,
        sort: sort.value,
      },
    }),
  {
    server: false,
    watch: [page, sort],
  },
);

const handleDelete = async () => {
  deleteing.value = true;
  await $fetch("/api/admin/users", {
    method: "DELETE",
    body: {
      userIds: selectedUserIds.value,
    },
  });
  deleteing.value = false;
  refresh();
};
</script>
<script>
import { LinkField } from "#components";

export default {
  components: {
    LinkField,
  },
};
</script>
<style scoped>
.admin-actions {
  margin: 12px;
}
</style>
