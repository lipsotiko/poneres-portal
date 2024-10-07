<template>
  <IContainer>
    <div class="top">
      <p class="lead">New Entity</p>
    </div>
    <ClientOnly>
      <IForm v-model="schema">
        <IRow>
          <IColumn xs="8">
            <IFormGroup required>
              <IFormLabel for="name">Name</IFormLabel>
              <IInput
                v-model="name"
                id="name"
                name="name"
                autocomplete
                :error="errorTypes"
              />
              <IFormError for="address" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn xs="4">
            <IFormGroup required>
              <IFormLabel for="email">Email</IFormLabel>
              <IInput
                v-model="email"
                id="email"
                name="email"
                autocomplete
                :error="errorTypes"
              />
              <IFormError for="email" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn xs="6">
            <IFormGroup required>
              <IFormLabel for="firstName">First name</IFormLabel>
              <IInput
                v-model="firstName"
                id="firstName"
                name="firstName"
                autocomplete
                :error="errorTypes"
              />
              <IFormError for="firstName" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn xs="6">
            <IFormGroup required>
              <IFormLabel for="lastName">Last name</IFormLabel>
              <IInput
                v-model="lastName"
                id="lastName"
                name="lastName"
                autocomplete
                :error="errorTypes"
              />
              <IFormError for="lastName" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
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
            <div class="create-entity">
              <div class="_display:flex _justify-content:space-between">
                <IButton to="/entities">Cancel</IButton>
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
  name: {
    validators: [
      {
        name: "required",
      },
    ],
  },
  email: {
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
  },
  firstName: {
    validators: [
      {
        name: "required",
      },
    ],
  },
  lastName: {
    validators: [
      {
        name: "required",
      },
    ],
  },
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

const name = ref();
const email = ref();
const firstName = ref();
const lastName = ref();
const address = ref();
const city = ref();
const state = ref();
const zipCode = ref();

const save = async () => {
  loading.value = true;
  await saveEntity({
    name: name.value,
    contactEmail: email.value,
    contactFirstName: firstName.value,
    contactLastName: lastName.value,
    contactAddress: address.value,
    contactCity: city.value,
    contactState: state.value,
    contactZipCode: zipCode.value,
  }).then(() => {
    navigateTo("/entities");
  });
};
</script>
<style>
.create-entity {
  margin-top: 18px;
  text-align: center;
}
</style>
