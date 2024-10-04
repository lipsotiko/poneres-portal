<template>
  <IContainer>
    <div class="top">
      <p class="lead">Properties</p>
      <IButton circle color="primary" to="/properties/new">
        <template #icon>
          <IIcon name="ink-plus" />
        </template>
      </IButton>
    </div>
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
const sort = ref("address,asc");
const colDefs = ref([
  {
    field: "address",
    width: 288,
    context: "PropertyLink",
    cellRenderer: "LinkField",
  },
  {
    field: "city",
    width: 120,
  },
  {
    field: "state",
    width: 80,
  },
  {
    field: "zipCode",
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
  "properties-created-by",
  () =>
    $fetch("/api/properties/created-by", {
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
