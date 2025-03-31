<template>
  <IContainer>
    <ClientOnly>
      <IForm v-model="schema">
        <IRow>
          <IColumn xs="12">
            <IFormGroup required>
              <IFormLabel for="email">Email address</IFormLabel>
              <IInput v-model="email" id="email" name="email" autocomplete :error="errorTypes">
                <template #append>
                  <i class="fa-regular fa-envelope"></i>
                </template>
              </IInput>
              <IFormError for="email" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn xs="6">
            <IFormGroup required>
              <IFormLabel for="firstName">First name</IFormLabel>
              <IInput v-model="firstName" id="firstName" name="firstName" autocomplete :error="errorTypes" />
              <IFormError for="firstName" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn xs="6">
            <IFormGroup required>
              <IFormLabel for="lastName">Last name</IFormLabel>
              <IInput v-model="lastName" id="lastName" name="lastName" autocomplete :error="errorTypes" />
              <IFormError for="lastName" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="password">Set a password</IFormLabel>
              <IInput v-model="password" id="password" name="password" type="password" autocomplete
                :error="errorTypes" />
              <IFormError for="password" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow v-if="!roles">
          <IColumn>
            <ICheckboxGroup id="roles" name="roles" :options="roleOptions" />
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <div class="create-account">
              <div>
                <IButton color="primary" :loading="loading" @click="createAccount"
                  :disabled="!schema.touched || schema.invalid">
                  Create account</IButton>
              </div>
            </div>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <div class="have-an-account">
              <p class="lead">
                Already have an account?
                <a :href="loginPage"> Sign in </a>
              </p>
            </div>
          </IColumn>
        </IRow>
      </IForm>
    </ClientOnly>
  </IContainer>
</template>
<script setup>
import { inject } from "vue";
import { useForm } from "@inkline/inkline/composables";

const route = useRoute()
const roles = route.query.roles;

const { showAdminRole } = defineProps(["showAdminRole"]);
const emit = defineEmits(["afterSubmit"]);

const roleOptions = ref([
  { id: 'ADMIN', label: 'Admin' },
  { id: 'OWNER', label: 'Owner' },
  { id: 'RESIDENT', label: 'Resident' },
]);

if (!showAdminRole) {
  roleOptions.value.shift();
}

const { schema } = useForm({
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
    validators: [{ name: "required" }],
  },
  lastName: {
    validators: [{ name: "required" }],
  },
  password: {
    validators: [
      {
        name: "required",
      },
      {
        name: "minLength",
        value: 8,
      },
      {
        name: "custom",
        message: "Password must contain at least one lower case, one upper case, and one special character.",
        validator: (password) => {
          var isValidPassword = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
          return isValidPassword.test(password);
        },
      },
    ],
  },
  roles: {
  }
});

const errorTypes = ["touched", "invalid"];
const loading = ref(false);
const errorMessage = inject("errorMessage");

const createAccount = async () => {
  loading.value = true;
  await signUpUser({
    email: schema.value.email.value,
    firstName: schema.value.firstName.value,
    lastName: schema.value.lastName.value,
    password: schema.value.password.value,
    roles: schema.value.roles.value || roles.split(","),
  })
    .then(() => {
      errorMessage.value = undefined;
      emit("afterSubmit");
    })
    .catch((err) => {
      errorMessage.value = err?.data?.message;
    });
  loading.value = false;
};
</script>
<style scoped>
.create-account {
  margin-top: 18px;
  text-align: center;
}

.have-an-account {
  text-align: center;
}
</style>
