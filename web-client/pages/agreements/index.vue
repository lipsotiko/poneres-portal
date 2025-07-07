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
            <BreadcrumbPage>Agreements</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>

  <div class="flex justify-end m-4">
    <div class="flex justify-end m-4">
      <Button @click="navigateTo('/agreements/new')">
        <Plus />
      </Button>
    </div>
  </div>
  <ag-grid-vue
    :loading="pending"
    :rowData="data?.content"
    :columnDefs="colDefs"
    :gridOptions="gridOptions"
    style="height: 888px"
    class="ag-theme-quartz"
  />
  <Pagination v-model="page" :items-total="data?.totalElements" :items-per-page="data?.size" />
  <Dialog :open="sendAgreementModal">
    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>Send agreement?</DialogTitle>
        <DialogDescription> Are you sure you want to send this agreement? </DialogDescription>
      </DialogHeader>
      <DialogFooter>
        <div class="w-full flex justify-between">
          <Button variant="destructive" @click="sendAgreementModal = false">No, don't</Button>
          <Button :disabled="sending" @click="handleSendForSigning">
            <Loader2 v-if="sending" class="w-4 h-4 animate-spin" />
            Yes, send it!
          </Button>
        </div>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
<script setup>
import "ag-grid-community/styles/ag-theme-quartz.css";
import { AgGridVue } from "ag-grid-vue3";
import { ref } from "vue";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Loader2, Plus } from "lucide-vue-next";
import { toast } from "vue-sonner";

const toastOpen = ref(false);
const sending = ref(false);
const sendAgreementModal = ref(false);
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
    context: {
      onSend: async (id) => {
        agreementToSendId.value = id;
        const testMode = await isTestMode();
        if (!testMode) {
          toast.error("System is NOT in test mode!", {
            description: "Are you sure you want to send this agreement?",
          });
        }
        sendAgreementModal.value = true;
      },
      onReminderSent: () => {
        toast.success("Reminder sent!");
      },
    },
    cellRenderer: "SignatureStatus",
    headerName: "Status / Action",
    width: 288,
  },
]);

const handleSendForSigning = async () => {
  sending.value = true;
  await sendForSigning(agreementToSendId.value);
  toast.success("Document sent for signing!");
  sendAgreementModal.value = false;
  sending.value = false;
  refresh();
};

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

const { pending, data, refresh } = await useLazyAsyncData(
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
