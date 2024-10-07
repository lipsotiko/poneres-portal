<template>
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
import { ref } from "vue";

const page = ref(1);

// TODO: Updated these when we know what fields will make sense.
const sort = ref("address,asc");
const colDefs = ref([
  {
    field: "client",
    context: "/agreements",
    cellRenderer: "LinkField",
    width: 288,
  },
  {
    colId: "contactFirstName",
    headerName: "Name",
    valueGetter: (p) => `${p.data.contactFirstName} ${p.data.contactLastName}`,
  },
  {
    field: "contactAddress",
    headerName: "Address",
    width: 288,
  },
  {
    field: "contactCity",
    headerName: "City",
    width: 120,
  },
  {
    field: "contactState",
    headerName: "State",
    width: 80,
  },
  {
    field: "contactZipCode",
    headerName: "Zip Code",
    width: 100,
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

const { pending, data } = await useAsyncData(
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
import { LinkField } from "#components";

export default {
  components: {
    LinkField,
  },
};
</script>
