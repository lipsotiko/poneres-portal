<template>
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
            <IInput id="firstName" name="firstName" autocomplete :error="errorTypes" />
            <IFormError for="firstName" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
        <IColumn xs="6">
          <IFormGroup required>
            <IFormLabel for="lastName">Last name</IFormLabel>
            <IInput id="lastName" name="lastName" autocomplete :error="errorTypes" />
            <IFormError for="lastName" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
      </IRow>
      <IRow>
        <IColumn>
          <IFormGroup required>
            <IFormLabel for="password">Set a password</IFormLabel>
            <IInput id="password" name="password" type="password" autocomplete
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
          <div class="text-center mt-4">
            <div>
              <Button @click="createAccount"
                :disabled="!schema.touched || schema.invalid || loading">
                <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
                Create account
              </Button>
            </div>
          </div>
        </IColumn>
      </IRow>
      <IRow>
        <IColumn>
          <div class="text-center">
            <p class="lead">
              Already have an account?
              <a :href="loginPage"> Sign in </a>
            </p>
          </div>
        </IColumn>
      </IRow>
      <IRow>
        <p class="text-sm font-thin">
          By clicking “Create Account", I agree to Poneres Connect's <a href="#">Terms of Use</a>, <a href="#">Privacy
            Policy</a> and to receive electronic communication about my accounts and services per Poneres Connect's
          <a href="#">Electronic Communications Agreement.</a>
        </p>
      </IRow>
    </IForm>
    <template #fallback>
      <!-- this will be rendered on server side -->
        <div class="flex justify-center">
        <Loader2 class="w-6 h-6 animate-spin" />
        </div>
    </template>
  </ClientOnly>
</template>
<script setup>
import { inject } from "vue";
import { useForm } from "@inkline/inkline/composables";
import { Button } from '@/components/ui/button'
import { Loader2 } from 'lucide-vue-next'

const route = useRoute();
const roles = route.query.roles;
const email = route.query.email;

// Clear the query params after navigation
const router = useRouter();
router.replace({ 'query': null })

const { showAdminRole } = defineProps(["showAdminRole"]);
const emit = defineEmits(["afterSubmit"]);

const roleOptions = ref([
  { id: "ADMIN", label: "Admin" },
  { id: "OWNER", label: "Owner" },
  { id: "RESIDENT", label: "Resident" },
  { id: "PROVIDER", label: "Provider" },
  { id: "FACILITY", label: "Facility" },
]);

if (!showAdminRole) {
  roleOptions.value.shift();
}

const { schema } = useForm({
  email: {
    value: email ? decodeURI(email) : undefined,
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
  },
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
