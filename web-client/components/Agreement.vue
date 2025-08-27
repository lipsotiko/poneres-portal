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
            <BreadcrumbLink as-child>
              <NuxtLink to="/agreements">
                Agreements
              </NuxtLink>
            </BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbPage>{{ `${title} - ${agreementId}` }}</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <div class="ml-6">
    <Form
      v-slot="{ meta, values, validate }"
      as=""
      ref="agreementForm"
      keep-values
      :validation-schema="toTypedSchema(schema)"
    >
      <form
        @submit="
          (e) => {
            e.preventDefault();
            validate();
            if (meta.valid) {
              onSubmit(values);
            }
          }
        "
      >
        <slot></slot>
        <div>
          <div>
            <div class="save">
              <div class="_display:flex _justify-content:space-between">
                <div class="left-buttons">
                  <Button
                    variant="secondary"
                    @click="(e) => loadTestData(e)"
                    v-if="$config.public.deploymentType === 'local'"
                    >Load Test Data</Button
                  >
                  <Button variant="secondary" v-if="!isNew" @click="(e) => handleCopy(e)" :disabled="copying"
                    >Copy</Button
                  >
                  <Button variant="secondary" @click="(e) => handlePreview(e)">Preview</Button>
                  <div class="m-2">
                    <Checkbox v-model="includeTestSignatures" />
                    <label class="m-2">Include signatures</label>
                  </div>
                </div>
                <div v-if="!loading" class="right-buttons">
                  <DeleteAgreementButton :id="agreementId" :isNew="isNew" />
                  <Button v-if="!sent" type="submit" :disabled="saving">
                    <Loader2 v-if="saving" class="w-4 h-4 animate-spin" />
                    Save
                  </Button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </form>
    </Form>
  </div>
</template>
<script setup>
import { useForm, useFieldArray } from "vee-validate";
import { z } from "zod/v4";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { toTypedSchema } from "@vee-validate/zod";
import { Form } from "@/components/ui/form";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Loader2 } from "lucide-vue-next";

const agreementForm = ref();

const { pdfType, title, agreementId, schema, testData } = defineProps([
  "pdfType",
  "title",
  "agreementId",
  "schema",
  "testData",
]);

const isNew = agreementId === "New";
const sent = ref();
const saving = ref(false);
const loading = ref(false);
const copying = ref(false);
const includeTestSignatures = ref(true);

useForm();
const { push: addRecipient } = useFieldArray("recipients");

onMounted(async () => {
  if (isNew) {
    return;
  }
  loading.value = true;

  const agreement = await getAgreement(agreementId);
  sent.value = agreement.sent;

  Object.keys(agreement.metadata).forEach((k) => {
    if (agreementForm.value.values.hasOwnProperty(k)) {
      agreementForm.value.setFieldValue(k, agreement.metadata[k]);
    }
  });

  agreement.recipients.forEach((recipient, index) => {
    if (index > 1) {
      console.log("here");
      addRecipient({
        name: undefined,
        email: undefined,
      });
    }
    agreementForm.value.setFieldValue(`recipients[${index}].name`, recipient.name);
    agreementForm.value.setFieldValue(`recipients[${index}].email`, recipient.email);
  });

  loading.value = false;
});

const hasMaxRecipients = () => {
  agreementForm.value?.values.recipients.length === 3;
};

const onSubmit = async (data) => {
  saving.value = true;

  let data2 = { ...data };
  const recipients = data.recipients;
  delete data2.recipients;

  await saveAgreement({
    id: isNew ? undefined : agreementId,
    type: pdfType,
    metadata: data2,
    recipients: recipients,
  }).then(() => {
    navigateTo("/agreements");
  });
};

const handleCopy = async (e) => {
  e.preventDefault();

  copying.value = true;
  await copyAgreement(agreementId).then(() => {
    navigateTo("/agreements");
  });
};

const handlePreview = (e) => {
  e.preventDefault();

  let data2 = { ...agreementForm.value.values };
  const recipients = agreementForm.value.values.recipients;
  delete data2.recipients;

  previewAgreement(pdfType, {
    metadata: data2,
    recipients: recipients,
    includeTestSignatures: includeTestSignatures.value,
  }).then((blob) => {
    var fileURL = window.URL.createObjectURL(blob);
    window.open(`${fileURL}`);
  });
};

const loadTestData = (e) => {
  e.preventDefault();
  Object.keys(testData).forEach((k) => {
    agreementForm.value.setFieldValue(k, testData[k]);
  });

  agreementForm.value.setFieldValue("recipients[0].name", "Hello World");
  agreementForm.value.setFieldValue("recipients[0].email", "evangelos.poneres@gmail.com");
  agreementForm.value.setFieldValue("recipients[1].name", "John Wick");
  agreementForm.value.setFieldValue("recipients[1].email", "evangelos@poneres.com");
};
</script>
<style>
.save {
  margin-top: 18px;
  text-align: center;
}

.left-buttons {
  display: flex;
}

.left-buttons button {
  margin: 6px;
}

.right-buttons button {
  margin: 6px;
}
</style>
