<template>
    <ILayout @keydown.esc="() => open = false">
        <ISidebar v-model="open" class="sidebar" collapse-position="fixed" :collapse="true" size="lg">
            <ILoader v-if="loadingPreview" />
            <IButton size="sm" @click="() => open = false">Close</IButton>
            <iframe ref="pdfPreview" class="preview-pdf">
            </iframe>
        </ISidebar>
        <IContainer>
            <PageTitle title="Residential Dwelling Lease Agreement - Maryland" backTo="/agreements/new" />
            <ClientOnly>
                <IForm v-model="schema">
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

                                    <IButton color="primary" :loading="loading" @click="save"
                                        :disabled="!schema.touched || schema.invalid">
                                        Save</IButton>
                                </div>
                            </div>
                        </IColumn>
                    </IRow>
                </IForm>
            </ClientOnly>
        </IContainer>
    </ILayout>
</template>
<script setup>
import { useForm } from "@inkline/inkline/composables";
const dayjs = useDayjs()
const fieldOptions = {
    validators: [
        {
            name: "required",
        }
    ]
}
const pdfType = 'LEASE_AGREEMENT_MD_V1';
const open = ref(false);
const loadingPreview = ref(false);
const pdfPreview = ref();

const { schema, form } = useForm({
    leaseOfferDate: fieldOptions,
    landlord: fieldOptions,
    tenant: fieldOptions,
    leasedAddress: fieldOptions
});

const errorTypes = ["touched", "invalid"];
const loading = ref(false);

const loadTestData = () => {
    schema.value.leaseOfferDate.value = dayjs().format('YYYY-MM-DD');
    schema.value.landlord.value = 'Evangelos Poneres'
    schema.value.tenant.value = 'Stephan Michael Nutty, Kristy Diane Nutty'
    schema.value.leasedAddress.value = '10722 LANCEWOOD RD, COCKEYSVILLE, MD 21030'
}

const preview = async () => {
    loadingPreview.value = true;
    open.value = true;
    await previewAgreement(pdfType, form.value).then(
        (blob) => {
            var file = window.URL.createObjectURL(blob);
            pdfPreview.value.src = file;
        },
    );
    loadingPreview.value = false;
}

const save = async () => {
    // loading.value = true;
    // await saveEntity({
    //   name: name.value,
    //   contactEmail: email.value,
    //   contactFirstName: firstName.value,
    //   contactLastName: lastName.value,
    //   contactAddress: address.value,
    //   contactCity: city.value,
    //   contactState: state.value,
    //   contactZipCode: zipCode.value,
    // }).then(() => {
    //   navigateTo("/entities");
    // });
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
