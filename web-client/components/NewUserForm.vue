<script setup>
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { toTypedSchema } from "@vee-validate/zod";
import { z } from "zod/v4";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Loader2 } from "lucide-vue-next";
import { AsYouType } from "libphonenumber-js";
import { toast } from "vue-sonner";

const route = useRoute();
const roles = route.query.roles;
const newUserForm = ref();

// Clear the query params after navigation
const router = useRouter();
router.replace({ query: null });

const { showAdminRole } = defineProps(["showAdminRole"]);
const emit = defineEmits(["afterSubmit"]);

const roleOptions = ref([
  { value: "ADMIN", label: "Admin" },
  { value: "OWNER", label: "Owner" },
  { value: "RESIDENT", label: "Resident" },
  { value: "PROVIDER", label: "Provider" },
  { value: "FACILITY", label: "Facility" },
]);

if (!showAdminRole) {
  roleOptions.value.shift();
}

let formSchema = z
  .object({
    email: z.email(),
    firstName: z.string(),
    lastName: z.string(),
    phoneNumber: z.string().check(({ value, issues}) => {
      console.log(value);
      var isValidPhoneNumber = new RegExp("^\\(\\d{3}\\) \\d{3}-\\d{4}$");
      if(!isValidPhoneNumber.test(value)) {
        issues.push({
          code: 'valid_phone_number',
          message: "Phone number must match the format: (###) ###-####.",
        });
      }
    }),
    password: z.string().min(8).check(({ value, issues }) => {
      var isValidPassword = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
      if (!isValidPassword.test(value)) {
        issues.push({
          code: 'valid_password',
          message: "Password must contain at least one lower case, one upper case, and one special character.",
        });
      }
    }),
    roles: z.record(z.string(), z.boolean()).check(({ value, issues }) => {
      if (Object.keys(value).length === 0 || !Object.values(value).some(v => v === true)) {
        issues.push({
          code: 'valid_roles',
          message: 'At least one role is required',
        })
      }
    }),
  });

let initialData = ref({
  email: undefined,
  firstName: undefined,
  lastName: undefined,
  phoneNumber: undefined,
  roles: {
    // ADMIN: true,
  }
});

const handlePhoneNumber = async (phoneNumber) => {
  const formatted = new AsYouType("US").input(phoneNumber);
  newUserForm.value.setFieldValue("phoneNumber", formatted);
};

const saving = ref(false);

const onSubmit = async (data) => {
  saving.value = true;
  await signUpUser(data)
    .then(() => {
      emit("afterSubmit");
    })
    .catch((err) => {
      toast.error(err?.data?.message);
    });
  saving.value = false;
};
</script>
<template>
  <Form
    v-slot="{ meta, values, validate }"
    ref="newUserForm"
    as=""
    keep-values
    :initial-values="initialData"
    :validation-schema="toTypedSchema(formSchema)"
  >
    <form
      class="max-w-lg"
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
      <div class="py-2">
        <FormField v-slot="{ componentField }" name="email">
          <FormItem>
            <FormLabel>Email</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="py-2">
        <FormField v-slot="{ componentField }" name="firstName">
          <FormItem>
            <FormLabel>First name</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="py-2">
        <FormField v-slot="{ componentField }" name="lastName">
          <FormItem>
            <FormLabel>Last name</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="py-2">
        <FormField v-slot="{ componentField }" name="phoneNumber">
          <FormItem>
            <FormLabel>Phone number</FormLabel>
            <FormControl>
              <Input v-bind="componentField" @update:modelValue="handlePhoneNumber" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="py-2">
        <FormField v-slot="{ componentField }" name="password">
          <FormItem>
            <FormLabel>Password</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="password" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div v-if="!roles" class="py-2">
        <div>
          <FormField v-slot="{ value, handleChange }" name="roles">
            <FormItem>
              <FormLabel>Roles</FormLabel>
              <FormControl>
                <CheckboxGroup :modelValue="value" :options="roleOptions" @update:modelValue="handleChange" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>
      </div>
      <div>
        <div>
          <div class="text-center mt-4">
            <div>
              <Button type="submit" :disabled="saving">
                <Loader2 v-if="saving" class="w-4 h-4 animate-spin" />
                Create account
              </Button>
            </div>
          </div>
        </div>
      </div>
      <div v-if="!showAdminRole">
        <div>
          <div class="text-center">
            <p class="lead">
              Already have an account?
              <a :href="loginPage"> Sign in </a>
            </p>
          </div>
        </div>
      </div>
      <div v-if="!showAdminRole">
        <p class="text-sm font-thin px-4">
          By clicking “Create Account", I agree to Shift Stat's <a href="#">Terms of Use</a>,
          <a href="#">Privacy Policy</a> and to receive electronic communication about my accounts and services per
          Shift Stat's
          <a href="#">Electronic Communications Agreement.</a>
        </p>
      </div>
    </form>
  </Form>
</template>
