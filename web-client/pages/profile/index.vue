<script setup>
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { toTypedSchema } from "@vee-validate/zod";
import { z } from "zod/v4";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Card } from "@/components/ui/card";
import { Loader2 } from "lucide-vue-next";
import { AsYouType } from "libphonenumber-js";
import { toast } from "vue-sonner";

// definePageMeta({
//   ssr: false,
// });

const profileForm = ref();

let formSchema = z.object({
  firstName: z.string(),
  lastName: z.string(),
  phoneNumber: z.string(),
  email: z.email(),
});

const { pending: pendingUserInfo, data: userInfo } = await useFetch("/api/user/info", {
  lazy: true,
  server: false,
  onResponse({ request, response, options }) {
    profileForm.value.setValues({
      firstName: response._data.userProfile.firstName,
      lastName: response._data.userProfile.lastName,
      phoneNumber: response._data.userProfile.phoneNumber,
      email: response._data.userProfile.email,
    });
  },
});

let initialData = ref({
  firstName: undefined,
  lastName: undefined,
  phoneNumber: undefined,
  email: undefined,
});

const saving = ref(false);

const handlePhoneNumber = async (phoneNumber) => {
  const formatted = new AsYouType("US").input(phoneNumber);
  profileForm.value.setFieldValue("phoneNumber", formatted);
};

async function onSubmit(data) {
  saving.value = true;
  await saveUserProfile(userInfo.value.userProfile.id, data).then(() => {
    saving.value = false;
  });
  toast.success("Profile saved!");
}

const verifyEmail = () => {
  sendVerificationEmail();
  toast("Please check your email for a verification link.");
};
</script>
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
            <BreadcrumbPage>Profile</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <Card class="m-2 p-4">
    <Form
      v-slot="{ meta, values, validate }"
      ref="profileForm"
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
          <FormField v-slot="{ componentField }" name="email">
            <FormItem>
              <FormLabel>Email</FormLabel>
              <FormControl>
                <div class="flex">
                  <Input v-bind="componentField" />
                  <Button v-if="!userInfo?.verified" variant="link" :disabled="pendingUserInfo" @click="verifyEmail">
                    <Loader2 v-if="pendingUserInfo" class="w-4 h-4 animate-spin" />
                    <span v-else>Verify</span>
                  </Button>
                </div>
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>
        <div class="flex justify-center m-4">
          <Button block color="primary" type="submit" :disabled="saving || pendingUserInfo">
            <Loader2 v-if="saving" class="w-4 h-4 animate-spin" />
            Save changes
          </Button>
        </div>
      </form>
    </Form>
  </Card>
</template>
