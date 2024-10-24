<template>
  <PdfPreview :key="metadataForm" :metadata="metadataForm" :type="pdfType" />
  <IContainer>
    <PageTitle :title="`Lead Paint Disclosure (Maryland) - ${agreementId}`" backTo="/agreements" />
    <ClientOnly>
      <IForm v-model="metaDataSchema" :disabled="loading">
        <IRow>
          <IColumn xs="6">
            <IFormGroup required>
              <IFormLabel for="landlord">Landlord</IFormLabel>
              <IInput id="landlord" name="landlord" :error="errorTypes" />
              <IFormError for="landlord" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn xs="6">
            <IFormGroup required>
              <IFormLabel for="tenant">Tenant</IFormLabel>
              <IInput id="tenant" name="tenant" :error="errorTypes" />
              <IFormError for="tenant" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn xs="12">
            <IFormGroup required>
              <IFormLabel for="leasedAddress">Leased address</IFormLabel>
              <IInput id="leasedAddress" name="leasedAddress" :error="errorTypes" />
              <IFormError for="leasedAddress" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn xs="4">
            <IFormGroup required>
              <IFormLabel for="leadKnown">Presence of lead paint?</IFormLabel>
              <IRadioGroup
                id="leadKnown"
                name="leadKnown"
                :options="[
                  { id: 'yes', label: 'Yes' },
                  { id: 'no', label: 'No' },
                ]"
              />
              <IFormError for="leadKnown" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup>
              <IFormLabel for="explain1"
                >Known lead-based paint and/or lead-based paint hazards are present in the housing
                (explain)</IFormLabel
              >
              <ITextarea id="explain1" name="explain1" :error="errorTypes" />
              <IFormError for="explain1" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn xs="4">
            <IFormGroup required>
              <IFormLabel for="documentsProvided">Records available to the lessor?</IFormLabel>
              <IRadioGroup
                id="documentsProvided"
                name="documentsProvided"
                :options="[
                  { id: 'yes', label: 'Yes' },
                  { id: 'no', label: 'No' },
                ]"
              />
              <IFormError for="leadKnown" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup>
              <IFormLabel for="explain2"
                >Lessor has provided the lessee with all available records and reports pertaining to lead-based paint
                and/or lead-based paint hazards in the housing</IFormLabel
              >
              <ITextarea id="explain2" name="explain2" :error="errorTypes" />
              <IFormError for="explain2" :visible="errorTypes" />
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
              <IInput :name="`recipients.${index}.name`" :error="errorTypes" />
              <IFormError :for="`recipients.${index}`" :visible="errorTypes" />
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
                  <IButton v-if="index > 1" size="sm" @click="removeRecipient(index)">
                    <i class="fa-solid fa-trash"></i>
                  </IButton>
                </template>
              </IInput>
              <IFormError :for="`recipients.${index}`" :visible="errorTypes" />
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
                <IButton @click="open = true">Preview</IButton>
              </div>
              <div v-if="!loading" class="right-buttons">
                <DeleteAgreementButton :id="agreementId" :isNew="isNew" />
                <IButton v-if="!sent" color="primary" :loading="saving" @click="save">Save</IButton>
              </div>
            </div>
          </div>
        </IColumn>
      </IRow>
    </ClientOnly>
  </IContainer>
</template>
<script setup>
import { provide } from 'vue'
import { useForm } from "@inkline/inkline/composables";
const {
  params: { id: agreementId },
} = useRoute();

const pdfType = "LEAD_PAINT_ADDENDUM_MD_V1";
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

const sent = ref();
const open = ref(false);

provide('sideBarOpen', open);

const {
  schema: metaDataSchema,
  form: metadataForm,
  validate,
} = useForm({
  landlord: fieldOptions,
  tenant: fieldOptions,
  leasedAddress: fieldOptions,
  leadKnown: {},
  explain1: {},
  documentsProvided: {},
  explain2: {}
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

const saving = ref(false);
const loading = ref(false);

const loadTestData = async () => {
  metaDataSchema.value.landlord.value = "Evangelos Poneres";
  metaDataSchema.value.tenant.value = "Stephan Michael Nutty, Kristy Diane Nutty";
  metaDataSchema.value.leasedAddress.value = "10722 LANCEWOOD RD, COCKEYSVILLE, MD 21030";
  metaDataSchema.value.leadKnown.value = "no";
  metaDataSchema.value.documentsProvided.value = "no";
  metaDataSchema.value.explain1.value = "Hello";
  metaDataSchema.value.explain2.value = "World";
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

.sidebar {
  --sidebar--width: 1100px;
}

input {
  text-transform: uppercase !important;
}

.remove-recipient {
  margin-top: 30px;
}

.inclusions .checkbox {
  display: inline;
}

.utilities .checkbox {
  display: inline;
  margin: 12px;
}
</style>
