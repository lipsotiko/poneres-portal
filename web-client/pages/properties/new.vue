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
              <IInput
                v-model="address"
                id="address"
                name="address"
                autocomplete
                :error="errorTypes"
              />
              <IFormError for="address" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="city">City</IFormLabel>
              <IInput
                v-model="city"
                id="city"
                name="city"
                autocomplete
                :error="errorTypes"
              />
              <IFormError for="city" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="state">State</IFormLabel>
              <ISelect
                v-model="state"
                id="state"
                name="state"
                :options="states"
                :error="errorTypes"
              />
              <IFormError for="state" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="zipCode">Zip code</IFormLabel>
              <IInput
                v-model="zipCode"
                id="zipCode"
                name="zipCode"
                maxlength="5"
                autocomplete
                :error="errorTypes"
              />
              <IFormError for="zipCode" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <div class="create-property">
              <div class="_display:flex _justify-content:space-between">
                <IButton to="/properties">Cancel</IButton>
                <IButton
                  color="primary"
                  :loading="loading"
                  @click="save"
                  :disabled="!schema.touched || schema.invalid"
                >
                  Save</IButton
                >
              </div>
            </div>
          </IColumn>
        </IRow>
      </IForm>
    </ClientOnly>
  </IContainer>
</template>
<script setup>
import { useForm } from "@inkline/inkline/composables";
const { schema } = useForm({
  address: {
    validators: [
      {
        name: "required",
      },
    ],
  },
  city: {
    validators: [
      {
        name: "required",
      },
    ],
  },
  state: {
    validators: [
      {
        name: "required",
      },
    ],
  },
  zipCode: {
    validators: [
      {
        name: "required",
      },
    ],
  },
});
const errorTypes = ["touched", "invalid"];
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
    zipCode: zipCode.value,
  }).then(() => {
    navigateTo("/properties");
  });
};
</script>
<style>
.create-property {
  margin-top: 18px;
  text-align: center;
}
</style>
