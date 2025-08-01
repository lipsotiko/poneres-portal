<script setup>
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
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
  { id: "ADMIN", label: "Admin" },
  { id: "PROVIDER", label: "Provider" },
  { id: "FACILITY", label: "Facility" },
]);

if (!showAdminRole) {
  roleOptions.value.shift();
}

let formSchema = z.object({
  email: z.email(),
  firstName: z.string(),
  lastName: z.string(),
  phoneNumber: z.string().check(({ value, issues }) => {
    var isValidPhoneNumber = new RegExp("^\\(\\d{3}\\) \\d{3}-\\d{4}$");
    if (!isValidPhoneNumber.test(value)) {
      issues.push({
        code: "valid_phone_number",
        message: "Phone number must match the format: (###) ###-####.",
      });
    }
  }),
  password: z
    .string()
    .min(8)
    .check(({ value, issues }) => {
      var isValidPassword = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
      if (!isValidPassword.test(value)) {
        issues.push({
          code: "valid_password",
          message: "Password must contain at least one lower case, one upper case, and one special character.",
        });
      }
    }),
  roles: z.array(z.string()).min(1),
});

let initialData = ref({
  email: undefined,
  firstName: undefined,
  lastName: undefined,
  phoneNumber: undefined,
  roles: [],
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
          <FormField name="roles">
            <FormItem>
              <FormLabel>Roles</FormLabel>
              <FormField
                v-for="item in roleOptions"
                v-slot="{ value, handleChange }"
                :key="item.id"
                type="checkbox"
                :value="item.id"
                :unchecked-value="false"
                name="roles"
              >
                <FormItem class="flex flex-row items-start space-y-0">
                  <FormControl>
                    <Checkbox :model-value="value.includes(item.id)" @update:model-value="handleChange" />
                  </FormControl>
                  <FormLabel class="font-normal">
                    {{ item.label }}
                  </FormLabel>
                </FormItem>
              </FormField>
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
            <p class="py-2">
              Already have an account?
              <NuxtLink :href="loginPage" class="hover:cursor-pointer hover:underline"> Sign in </NuxtLink>
            </p>
          </div>
        </div>
      </div>
      <div v-if="!showAdminRole">
        <p class="text-sm font-light px-4 py-2">
          By clicking “Create Account", I agree to Shift Stat's <a href="#">Terms of Use</a>,
          <a href="#">Privacy Policy</a> and to receive electronic communication about my accounts and services per
          Shift Stat's
          <a href="#">Electronic Communications Agreement.</a>
        </p>
      </div>
    </form>
  </Form>
</template>
