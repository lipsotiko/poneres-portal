<template>
    <IContainer>
        <PageTitle title="Admin panel" backTo="/" />
        <ag-grid-vue :loading="pending" :rowData="data.content" :columnDefs="colDefs" :gridOptions="gridOptions"
            style="height: 500px" class="ag-theme-quartz" />
    </IContainer>
</template>
<script setup>
import "ag-grid-community/styles/ag-theme-quartz.css"; // Optional Theme applied to the Data Grid
import { AgGridVue } from "ag-grid-vue3"; // Vue Data Grid Component

const colDefs = ref([
    { field: 'id' },
    {
        headerName: 'Name',
        valueGetter: (p) => `${p.data.firstName} ${p.data.lastName}`
    },
    { field: 'email' },
    { field: 'roles' },
    { field: 'authProviderId' },
    { field: 'paymentProviderId' }
]);

const gridOptions = {
    enableCellTextSelection: true
}

const { pending, data } = await useAsyncData(
    'properties-created-by',
    () =>
        $fetch('/api/user')
    , {
        server: false
    }
);
</script>
