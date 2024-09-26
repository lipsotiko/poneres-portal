<template>
    <IContainer>
        <div class="top">
            <p class="lead">Properties</p>
        </div>
        <ag-grid-vue :rowData="rowData" :columnDefs="colDefs" style="height: 500px" class="ag-theme-quartz" />
    </IContainer>
</template>
<script setup>
import "ag-grid-community/styles/ag-theme-quartz.css"; // Optional Theme applied to the Data Grid
import { AgGridVue } from "ag-grid-vue3"; // Vue Data Grid Component
import { ref } from 'vue';
// Row Data: The data to be displayed.
const rowData = ref([
    { address: '227 Willow Ave.', city: 'Towson', state: 'MD', zipCode: '21286' },
    { address: '1037 Wingate Ct.', city: 'Bel Air', state: 'MD', zipCode: '21014' },
    { address: '2307 Oakmont Rd.', city: 'Fallston', state: 'MD', zipCode: '21047' },
]);

// Column Definitions: Defines the columns to be displayed.
const colDefs = ref([
    { field: "address" },
    { field: "city" },
    { field: "state" },
    { field: "zipCode" }
]);
</script>
