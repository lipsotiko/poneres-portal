<template>
    <PdfPreview :key="getKey()" :metadata="metadataForm" :recipients="recipientsForm.recipients" :type="pdfType" :includeTestSignatures="includeTestSignatures" />
    <IContainer>
        <PageTitle :title="`${title} - ${agreementId}`" backTo="/agreements" />
        <IRow>
            <p class="lead">Agreement</p>
        </IRow>
        <ClientOnly>
            <IForm v-model="metaDataSchema" :disabled="loading || sent">
                <slot></slot>
            </IForm>
        </ClientOnly>
        <Recipients v-model="recipientsSchema" :recipientName-1="recipientName1" :recipientEmail-1="recipientEmail1"
            :recipientName-2="recipientName2" :recipientEmail-2="recipientEmail2" />
        <IRow>
            <IColumn>
                <div class="save">
                    <div class="_display:flex _justify-content:space-between">
                        <div class="left-buttons">
                            <IButton @click="loadTestData()" v-if="$config.public.deploymentType === 'local'">Load Test
                                Data</IButton>
                            <IButton v-if="!isNew" @click="handleCopy()" :loading="copying">Copy</IButton>
                            <IButton @click="addRecipient()" :disabled="recipientsForm.recipients.length === 3">Add
                                recipient</IButton>
                            <IButton @click="open = true">Preview</IButton>
                            <ICheckbox v-model="includeTestSignatures">Include signatures</ICheckbox>
                        </div>
                        <div v-if="!loading" class="right-buttons">
                            <DeleteAgreementButton :id="agreementId" :isNew="isNew" />
                            <IButton v-if="!sent" color="primary" :loading="saving" @click="save()">Save</IButton>
                        </div>
                    </div>
                </div>
            </IColumn>
        </IRow>
    </IContainer>
</template>
<script setup>
import { inject } from "vue";
import { useForm } from "@inkline/inkline/composables";
const { pdfType, title, agreementId, schema, testData, recipientName1, recipientEmail1, recipientName2, recipientEmail2 } = defineProps([
    "pdfType",
    "title",
    "agreementId",
    "schema",
    "testData",
    "recipientName-1",
    "recipientEmail-1",
    "recipientName-2",
    "recipientEmail-2"
]);

const isNew = agreementId === "New";
const sent = ref();
const saving = ref(false);
const loading = ref(false);
const copying = ref(false);
const includeTestSignatures = ref(true);

const open = inject("sideBarOpen");

const { schema: metaDataSchema, form: metadataForm, validate: metadataValidate } = useForm({ ...schema });

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

const addRecipient = () => {
    recipientsSchema.value.recipients.push({
        name: { ...fieldOptions },
        email: { ...emailFieldOptions },
    });
};

const getKey = () => JSON.stringify(metadataForm.value) + JSON.stringify(recipientsForm.value) + JSON.stringify(includeTestSignatures.value);

onMounted(async () => {
    if (isNew) {
        return;
    }
    loading.value = true;

    const agreement = await getAgreement(agreementId);
    sent.value = agreement.sent;

    Object.keys(agreement.metadata).forEach((k) => {
        if (metaDataSchema.value.hasOwnProperty(k)) {
            metaDataSchema.value[k].value = agreement.metadata[k];
        }
    });

    agreement.recipients.forEach((recipient, index) => {
        if (index > 1) {
            addRecipient(recipientsSchema);
        }
        recipientsSchema.value.recipients[index].email.value = recipient.email;
        recipientsSchema.value.recipients[index].name.value = recipient.name;
    });

    loading.value = false;
});

const save = async () => {
    await metadataValidate();
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

const handleCopy = async () => {
    copying.value = true;
    await copyAgreement(agreementId).then(() => {
        navigateTo("/agreements");
    });
}

const loadTestData = () => {
    Object.keys(testData).forEach((k) => {
        metaDataSchema.value[k].value = testData[k];
    });
    metaDataSchema.value.touched = true;
    recipientsSchema.value.recipients[0].name.value = "Hello World";
    recipientsSchema.value.recipients[0].email.value = "evangelos.poneres@gmail.com";
    recipientsSchema.value.recipients[1].name.value = "Hello World";
    recipientsSchema.value.recipients[1].email.value = "hello@poneres.com";
    recipientsSchema.value.touched = true;
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