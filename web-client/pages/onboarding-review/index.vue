<template>
  <DefaultLayoutWrapper>
    <template #breadcrumbs>
      <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbLink href="/"> Home </BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbPage>Onboarding Review</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <ag-grid-vue :loading="pending" :rowData="data?.content" :columnDefs="colDefs" :gridOptions="gridOptions"
    :rowSelection="rowSelection" :getRowId="(params) => String(params.data.id)" style="height: 888px"
    class="ag-theme-quartz" />
  <div class="_display:flex _justify-content:space-between">
    <IPagination v-model="page" :items-total="data?.totalElements" :items-per-page="data?.size" />
    <div class="admin-actions">
      <IButton outline size="sm" color="danger" :disabled="selectedOnboardingIds.length === 0" :loading="deleteing"
        @click="handleDelete()">Delete
      </IButton>
    </div>
  </div>
</template>
<script setup>
import "ag-grid-community/styles/ag-theme-quartz.css";
import { AgGridVue } from "ag-grid-vue3";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";

const deleteing = ref(false);
const page = ref(1);
const sort = ref("firstName,asc");
const colDefs = ref([
  {
    colId: "name",
    headerName: "Name",
    valueGetter: (p) => `${p.data.firstName} ${p.data.middleName} ${p.data.lastName}`,
    context: "/onboarding-review",
    cellRenderer: "LinkField",
    width: 268,
  },
  { field: "location", width: 100 },
  { field: "dob" },
  { field: "specialty" },
  { field: "npi" }
]);

const rowSelection = {
  mode: "multiRow",
};

const selectedOnboardingIds = ref([]);

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
    selectedOnboardingIds.value = e.api.getSelectedNodes().map(({ data: { id } }) => id);
  },
};

const { pending, data, refresh } = await useAsyncData(
  "properties-created-by",
  () =>
    $fetch("/api/onboarding", {
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
  await $fetch("/api/onboarding/multiple", {
    method: "DELETE",
    body: {
      onboardingIds: selectedOnboardingIds.value,
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
