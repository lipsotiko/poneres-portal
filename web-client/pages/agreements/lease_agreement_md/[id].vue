<template>
  <IToast v-if="errorMessage" color="warning">
    <p>{{ errorMessage }}</p>
  </IToast>
  <ILayout @keydown.esc="() => (open = false)">
    <ISidebar v-model="open" class="sidebar" collapse-position="fixed" :collapse="true" size="lg">
      <ILoader v-if="loadingPreview" />
      <IButton v-else size="sm" @click="() => (open = false)">Close</IButton>
      <iframe ref="pdfPreview" class="preview-pdf"> </iframe>
    </ISidebar>
    <IContainer>
      <PageTitle :title="`Residential Dwelling Lease (Maryland) - ${agreementId}`" backTo="/agreements" />
      <ClientOnly>
        <IForm v-model="schema" :disabled="loading">
          <IRow>
            <IColumn xs="2">
              <IFormGroup required>
                <IFormLabel for="leaseOfferDate">Lease Date</IFormLabel>
                <IInput id="leaseOfferDate" name="leaseOfferDate" type="date" :error="errorTypes" />
                <IFormError for="leaseOfferDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="5">
              <IFormGroup required>
                <IFormLabel for="landlord">Landlord</IFormLabel>
                <IInput id="landlord" name="landlord" :error="errorTypes" />
                <IFormError for="landlord" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="5">
              <IFormGroup required>
                <IFormLabel for="tenant">Tenant</IFormLabel>
                <IInput id="tenant" name="tenant" :error="errorTypes" />
                <IFormError for="tenant" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="6">
              <IFormGroup required>
                <IFormLabel for="leasedAddress">Leased Address</IFormLabel>
                <IInput id="leasedAddress" name="leasedAddress" :error="errorTypes" />
                <IFormError for="leasedAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <div class="save">
                <div class="_display:flex _justify-content:space-between">
                  <div class="left-buttons">
                    <IButton to="/agreements">Cancel</IButton>
                    <IButton @click="loadTestData()">Load Test Data</IButton>
                    <IButton @click="preview()">Preview</IButton>
                  </div>
                  <div v-if="!loading" class="right-buttons">
                    <IButton color="danger" @click="deleteModalVisible = true" :disabled="agreementId === 'New'"
                      :loading="deleting">Delete</IButton>
                    <IButton v-if="!sent" color="primary" :loading="saving" @click="save"
                      :disabled="!schema.touched || schema.invalid">
                      Save</IButton>
                  </div>

                </div>
              </div>
            </IColumn>
          </IRow>
        </IForm>
        <IModal v-model="deleteModalVisible">
          <template #header>Delete agreement?</template>
          <span>Are you sure you want to delete this agreement?</span>
          <template #footer>
            <div style="display: flex; justify-content: space-between">
              <IButton outline color="dark" @click="deleteModalVisible = false">No, keep it.</IButton>
              <IButton color="danger" @click="handleDelete()" :loading="deleting">Yes, delete!</IButton>
            </div>
          </template>
        </IModal>
      </ClientOnly>
    </IContainer>
  </ILayout>
</template>
<script setup>
import { useForm } from "@inkline/inkline/composables";
const dayjs = useDayjs();
const {
  params: { id: agreementId },
} = useRoute();
const fieldOptions = {
  validators: [
    {
      name: "required",
    },
  ],
};
const errorMessage = ref();
const pdfType = "LEASE_AGREEMENT_MD_V1";
const sent = ref();
const open = ref(false);
const loadingPreview = ref(false);
const deleteModalVisible = ref(false);
const deleting = ref(false);
const pdfPreview = ref();

const { schema, form } = useForm({
  leaseOfferDate: fieldOptions,
  landlord: fieldOptions,
  tenant: fieldOptions,
  leasedAddress: fieldOptions,
});

const errorTypes = ["touched", "invalid"];
const saving = ref(false);
const loading = ref(false);

const loadTestData = async () => {
  schema.value.leaseOfferDate.value = dayjs().format("YYYY-MM-DD");
  schema.value.landlord.value = "Evangelos Poneres";
  schema.value.tenant.value = "Stephan Michael Nutty, Kristy Diane Nutty";
  schema.value.leasedAddress.value =
    "10722 LANCEWOOD RD, COCKEYSVILLE, MD 21030";
  schema.value.touched = true;
};

onMounted(async () => {
  if (agreementId === 'New') {
    return;
  }
  loading.value = true;

  const agreement = await getAgreement(agreementId);
  sent.value = agreement.sent;
  Object.keys(agreement.metadata).forEach((k) => {
    schema.value[k].value = agreement.metadata[k];
  });

  loading.value = false;
});

const preview = async () => {
  pdfPreview.value.src = '';
  loadingPreview.value = true;
  open.value = true;
  await previewAgreement(pdfType, form.value).then((blob) => {
    var file = window.URL.createObjectURL(blob);
    pdfPreview.value.src = file;
  });
  loadingPreview.value = false;
};

const handleDelete = () => {
  deleting.value = true;
  deleteAgreement(agreementId)
    .then(() => {
      navigateTo("/agreements");
    })
    .catch((err) => {
      errorMessage.value = err.data.message;
      deleteModalVisible.value = false;
      deleting.value = false;
    });
};

const save = async () => {
  saving.value = true;
  await saveAgreement({
    id: agreementId === "New" ? undefined : agreementId,
    type: pdfType,
    metadata: form.value,
  }).then(() => {
    navigateTo("/agreements");
  });
};
</script>
<style scoped>
.save {
  margin-top: 18px;
  text-align: center;
}

.left-buttons button {
  margin: 6px;
}

.right-buttons button {
  margin: 6px;
}

.preview-pdf {
  height: 100%;
}

.sidebar {
  --sidebar--width: 1100px;
}

input {
  text-transform: uppercase !important;
}
</style>
