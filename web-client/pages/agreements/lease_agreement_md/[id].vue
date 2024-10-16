<template>
  <IToast v-if="errorMessage" color="warning">
    <p>{{ errorMessage }}</p>
  </IToast>
  <ILayout @keydown.esc="() => (open = false)">
    <ISidebar
      v-model="open"
      class="sidebar"
      collapse-position="fixed"
      :collapse="true"
      size="lg"
    >
      <ILoader v-if="loadingPreview" />
      <IButton v-else size="sm" @click="() => (open = false)">Close</IButton>
      <iframe ref="pdfPreview" class="preview-pdf"> </iframe>
    </ISidebar>
    <IContainer>
      <PageTitle
        :title="`Residential Dwelling Lease (Maryland) - ${agreementId}`"
        backTo="/agreements"
      />
      <ClientOnly>
        <IForm v-model="metaDataSchema" :disabled="loading">
          <IRow>
            <p class="lead">Agreement</p>
          </IRow>
          <IRow>
            <IColumn xs="2">
              <IFormGroup required>
                <IFormLabel for="leaseOfferDate">Offer Date</IFormLabel>
                <IInput
                  id="leaseOfferDate"
                  name="leaseOfferDate"
                  type="date"
                  :error="errorTypes"
                />
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
                <IInput
                  id="leasedAddress"
                  name="leasedAddress"
                  :error="errorTypes"
                />
                <IFormError for="leasedAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="2">
              <IFormGroup required>
                <IFormLabel for="termLength">Lease Term</IFormLabel>
                <IInput
                  id="termLength"
                  name="termLength"
                  placeholder="Years / Months"
                  :error="errorTypes"
                />
                <IFormError for="termLength" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="2">
              <IFormGroup required>
                <IFormLabel for="startDate">Start Date</IFormLabel>
                <IInput
                  id="startDate"
                  name="startDate"
                  type="date"
                  :error="errorTypes"
                />
                <IFormError for="startDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="2">
              <IFormGroup required>
                <IFormLabel for="endDate">End Date</IFormLabel>
                <IInput
                  id="endDate"
                  name="endDate"
                  type="date"
                  :error="errorTypes"
                />
                <IFormError for="endDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
        </IForm>
        <IRow>
          <p class="lead">Recipients</p>
        </IRow>
        <IForm v-model="recipientsSchema" :disabled="loading">
          <IRow v-for="(_, index) in recipientsSchema.recipients" :key="index">
            <IColumn xs="6">
              <IFormGroup required>
                <IFormLabel :for="`recipients.${index}.name`">
                  <span v-if="index === 0">Landlord name</span>
                  <span v-else>Tenant name {{ index }}</span>
                </IFormLabel>
                <IInput
                  :name="`recipients.${index}.name`"
                  :error="errorTypes"
                />
                <IFormError
                  :for="`recipients.${index}`"
                  :visible="errorTypes"
                />
              </IFormGroup>
            </IColumn>
            <IColumn xs="6">
              <IFormGroup required>
                <IFormLabel :for="`recipients.${index}.email`">
                  <span v-if="index === 0">Landlord email</span>
                  <span v-else>Tenant email {{ index }}</span>
                </IFormLabel>
                <IInput :name="`recipients.${index}.email`" :error="errorTypes">
                  <template #append>
                    <IButton
                      v-if="index > 1"
                      size="sm"
                      @click="removeRecipient(index)"
                    >
                      <i class="fa-solid fa-trash"></i>
                    </IButton>
                  </template>
                </IInput>
                <IFormError
                  :for="`recipients.${index}`"
                  :visible="errorTypes"
                />
              </IFormGroup>
            </IColumn>
          </IRow>
        </IForm>
        <IRow>
          <IColumn>
            <div class="save">
              <div class="_display:flex _justify-content:space-between">
                <div class="left-buttons">
                  <IButton @click="loadTestData()">Load Test Data</IButton>
                  <IButton @click="addRecipient">Add recipient</IButton>
                  <IButton @click="preview()">Preview</IButton>
                </div>
                <div v-if="!loading" class="right-buttons">
                  <IButton
                    color="danger"
                    @click="deleteModalVisible = true"
                    :disabled="isNew"
                    :loading="deleting"
                  >
                    Delete
                  </IButton>
                  <IButton
                    v-if="!sent"
                    color="primary"
                    :loading="saving"
                    @click="save"
                    >Save</IButton
                  >
                </div>
              </div>
            </div>
          </IColumn>
        </IRow>
        <IModal v-model="deleteModalVisible">
          <template #header>Delete agreement?</template>
          <span>Are you sure you want to delete this agreement?</span>
          <template #footer>
            <div style="display: flex; justify-content: space-between">
              <IButton outline color="dark" @click="deleteModalVisible = false"
                >No, keep it.</IButton
              >
              <IButton
                color="danger"
                @click="handleDelete()"
                :loading="deleting"
                >Yes, delete!</IButton
              >
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

const pdfType = "LEASE_AGREEMENT_MD_V1";
const isNew = agreementId === "New";

const fieldOptions = {
  validators: [
    {
      name: "required",
    },
  ],
};

const emailFieldOptions = {
  validators: [
    {
      name: "required",
    },
    {
      name: "custom",
      message: "Please enter a valid email address.",
      validator: emailValidator,
    },
  ],
};

const errorMessage = ref();
const sent = ref();
const open = ref(false);
const loadingPreview = ref(false);
const deleteModalVisible = ref(false);
const deleting = ref(false);
const pdfPreview = ref();

const {
  schema: metaDataSchema,
  form: metadataForm,
  validate,
} = useForm({
  leaseOfferDate: fieldOptions,
  landlord: fieldOptions,
  tenant: fieldOptions,
  leasedAddress: fieldOptions,
  termLength: fieldOptions,
  startDate: fieldOptions,
  endDate: fieldOptions,
});

const {
  schema: recipientsSchema,
  form: recipientsForm,
  validate: recipientsValidate,
} = useForm({
  recipients: [
    {
      name: fieldOptions,
      email: emailFieldOptions,
    },
    {
      name: fieldOptions,
      email: emailFieldOptions,
    },
  ],
});

const errorTypes = ["touched", "invalid"];
const saving = ref(false);
const loading = ref(false);

const loadTestData = async () => {
  metaDataSchema.value.leaseOfferDate.value = dayjs().format("YYYY-MM-DD");
  metaDataSchema.value.landlord.value = "Evangelos Poneres";
  metaDataSchema.value.tenant.value = "Stephan Michael Nutty, Kristy Diane Nutty";
  metaDataSchema.value.leasedAddress.value = "10722 LANCEWOOD RD, COCKEYSVILLE, MD 21030";
  metaDataSchema.value.termLength.value = "1 Year";
  metaDataSchema.value.startDate.value = dayjs().format("YYYY-MM-DD");
  metaDataSchema.value.endDate.value = dayjs().format("YYYY-MM-DD");
  metaDataSchema.value.touched = true;

  recipientsSchema.value.recipients[0].name.value = "John Wick";
  recipientsSchema.value.recipients[0].email.value = "landlord@poneres.com";
  recipientsSchema.value.recipients[1].name.value = "Stephan Michael Nutty";
  recipientsSchema.value.recipients[1].email.value = "tenant_a@hello.io";
  recipientsSchema.value.touched = true;
};

onMounted(async () => {
  if (isNew) {
    return;
  }
  loading.value = true;

  const agreement = await getAgreement(agreementId);
  sent.value = agreement.sent;
  Object.keys(agreement.metadata).forEach((k) => {
    metaDataSchema.value[k].value = agreement.metadata[k];
  });

  agreement.recipients.forEach((recipient, index) => {
    if (index > 1) {
      addRecipient();
    }
    recipientsSchema.value.recipients[index].email.value = recipient.email;
    recipientsSchema.value.recipients[index].name.value = recipient.name;
  });

  loading.value = false;
});

const preview = async () => {
  pdfPreview.value.src = "";
  loadingPreview.value = true;
  open.value = true;
  await previewAgreement(pdfType, metadataForm.value).then((blob) => {
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
  await validate();
  await recipientsValidate();

  if (!metaDataSchema.value.valid || !recipientsSchema.value.valid) {
    return;
  }

  saving.value = true;

  await saveAgreement({
    id: isNew ? undefined : agreementId,
    type: pdfType,
    metadata: metadataForm.value,
    recipients: recipientsForm.value.recipients,
  }).then(() => {
    navigateTo("/agreements");
  });
};

const addRecipient = () => {
  recipientsSchema.value.recipients.push({
    name: { ...fieldOptions },
    email: { ...emailFieldOptions },
  });
};

const removeRecipient = (index) => {
  if (recipientsSchema.value.recipients.length === 1) {
    return;
  }
  recipientsSchema.value.recipients.splice(index, 1);
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

.remove-recipient {
  margin-top: 30px;
}
</style>
