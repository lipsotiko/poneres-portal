<template>
  <IContainer>
    <PageTitle title="Admin panel" backTo="/" />
    <ag-grid-vue
      :loading="pending"
      :rowData="data?.content"
      :columnDefs="colDefs"
      :gridOptions="gridOptions"
      :columnOptions="{
        sortChanged: (e) => console.log(e),
      }"
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

const page = ref(1);
const sort = ref("firstName,asc");
const colDefs = ref([
  { field: "id" },
  {
    colId: "firstName",
    headerName: "Name",
    valueGetter: (p) => `${p.data.firstName} ${p.data.lastName}`,
  },
  { field: "email" },
  { field: "roles" },
  { field: "authProviderId" },
  { field: "paymentProviderId" },
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

const { pending, data } = await useAsyncData(
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
</script>
