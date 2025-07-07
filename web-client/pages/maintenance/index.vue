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
            <BreadcrumbPage>Maintenance</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <div class="flex justify-end m-4">
    <Button @click="navigateTo('/maintenance/new')">
      <Plus />
    </Button>
  </div>
  <ag-grid-vue
    :loading="pending"
    :rowData="data?.content"
    :columnDefs="colDefs"
    :gridOptions="gridOptions"
    @rowClicked="onRowClicked"
    style="height: 888px"
    class="ag-theme-quartz"
  />
  <Pagination v-model="page" :items-total="data?.totalElements" :items-per-page="data?.size" />
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
import { Button } from "@/components/ui/button";
import { Loader2, Plus } from "lucide-vue-next";

const page = ref(1);
const sort = ref("createdAt,asc");

const colDefs = ref([
  {
    field: "title",
    width: 288,
  },
  {
    field: "description",
    flex: 1,
  },
  {
    field: "status",
    width: 128,
  },
  {
    field: "type",
    width: 108,
  },
  {
    field: "priority",
    width: 108,
  },
  {
    field: "costEstimate",
    width: 148,
    valueGetter: (p) => `$${p.data.costEstimate}`,
  },
  {
    field: "createdAt",
    width: 222,
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

const onRowClicked = ({ data: { id } }) => {
  navigateTo(`/maintenance/${id}`);
};

const { pending, data, refresh } = await useLazyAsyncData(
  "maintenance",
  () =>
    $fetch("/api/maintenance", {
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
