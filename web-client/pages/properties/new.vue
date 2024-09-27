<template>
    <IContainer>
        <div class="top">
            <p class="lead">New Property</p>
        </div>
        <ClientOnly>
            <IForm v-model="schema">
                <IRow>
                    <IColumn>
                        <IFormGroup required>
                            <IFormLabel for="address">Address</IFormLabel>
                            <IInput v-model="address" id="address" name="address" autocomplete :error="errorTypes" />
                            <IFormError for="address" :visible="errorTypes" />
                        </IFormGroup>
                    </IColumn>
                </IRow>
                <IRow>
                    <IColumn>
                        <IFormGroup required>
                            <IFormLabel for="city">City</IFormLabel>
                            <IInput v-model="city" id="city" name="city" autocomplete :error="errorTypes" />
                            <IFormError for="city" :visible="errorTypes" />
                        </IFormGroup>
                    </IColumn>
                    <IColumn>
                        <IFormGroup required>
                            <IFormLabel for="state">State</IFormLabel>
                            <ISelect v-model="state" id="state" name="state" :options="states" :error="errorTypes" />
                            <IFormError for="state" :visible="errorTypes" />
                        </IFormGroup>
                    </IColumn>
                    <IColumn>
                        <IFormGroup required>
                            <IFormLabel for="zipCode">Zip code</IFormLabel>
                            <IInput v-model="zipCode" id="zipCode" name="zipCode" maxlength="5" autocomplete
                                :error="errorTypes" />
                            <IFormError for="zipCode" :visible="errorTypes" />
                        </IFormGroup>
                    </IColumn>
                </IRow>
                <IRow>
                    <IColumn>
                        <div class="create-property">
                            <div class="_display:flex _justify-content:space-between">
                                <IButton to="/properties">Cancel</IButton>
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
</template>
<script setup>
import { useForm } from '@inkline/inkline/composables';
const { schema } = useForm({
    address: {
        validators: [
            {
                name: 'required'
            }
        ]
    },
    city: {
        validators: [
            {
                name: 'required'
            }
        ]
    },
    state: {

        validators: [
            {
                name: 'required'
            }
        ]
    },
    zipCode: {
        validators: [
            {
                name: 'required'
            }
        ]
    }
});
const errorTypes = ['touched', 'invalid'];
const loading = ref(false);

const address = ref();
const city = ref();
const state = ref();
const zipCode = ref();

const save = async () => {
    loading.value = true;
    await saveProperty({
        address: address.value,
        city: city.value,
        state: state.value,
        zipCode: zipCode.value
    }).then(() => {
        navigateTo('/properties');
    })
}

const states = [
    { id: 'AL', label: 'Alabama' },
    { id: 'AK', label: 'Alaska' },
    { id: 'AZ', label: 'Arizona' },
    { id: 'AR', label: 'Arkansas' },
    { id: 'CA', label: 'California' },
    { id: 'CO', label: 'Colorado' },
    { id: 'CT', label: 'Connecticut' },
    { id: 'DE', label: 'Delaware' },
    { id: 'DC', label: 'District of Columbia' },
    { id: 'FL', label: 'Florida' },
    { id: 'GA', label: 'Georgia' },
    { id: 'HI', label: 'Hawaii' },
    { id: 'ID', label: 'Idaho' },
    { id: 'IL', label: 'Illinois' },
    { id: 'IN', label: 'Indiana' },
    { id: 'IA', label: 'Iowa' },
    { id: 'KS', label: 'Kansas' },
    { id: 'KY', label: 'Kentucky' },
    { id: 'LA', label: 'Louisiana' },
    { id: 'ME', label: 'Maine' },
    { id: 'MD', label: 'Maryland' },
    { id: 'MA', label: 'Massachusetts' },
    { id: 'MI', label: 'Michigan' },
    { id: 'MN', label: 'Minnesota' },
    { id: 'MS', label: 'Mississippi' },
    { id: 'MO', label: 'Missouri' },
    { id: 'MT', label: 'Montana' },
    { id: 'NE', label: 'Nebraska' },
    { id: 'NV', label: 'Nevada' },
    { id: 'NH', label: 'New Hampshire' },
    { id: 'NJ', label: 'New Jersey' },
    { id: 'NM', label: 'New Mexico' },
    { id: 'NY', label: 'New York' },
    { id: 'NC', label: 'North Carolina' },
    { id: 'ND', label: 'North Dakota' },
    { id: 'OH', label: 'Ohio' },
    { id: 'OK', label: 'Oklahoma' },
    { id: 'OR', label: 'Oregon' },
    { id: 'PA', label: 'Pennsylvania' },
    { id: 'RI', label: 'Rhode Island' },
    { id: 'SC', label: 'South Carolina' },
    { id: 'SD', label: 'South Dakota' },
    { id: 'TN', label: 'Tennessee' },
    { id: 'TX', label: 'Texas' },
    { id: 'UT', label: 'Utah' },
    { id: 'VT', label: 'Vermont' },
    { id: 'VA', label: 'Virginia' },
    { id: 'WA', label: 'Washington' },
    { id: 'WV', label: 'West Virginia' },
    { id: 'WI', label: 'Wisconsin' },
    { id: 'WY', label: 'Wyoming' }
]

</script>
<style>
.create-property {
    margin-top: 18px;
    text-align: center;
}
</style>
