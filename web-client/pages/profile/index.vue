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

const profileForm = ref();

let formSchema = z.object({
  firstName: z.string(),
  lastName: z.string(),
  phoneNumber: z.string(),
  email: z.email(),
  profilePhoto: z.file().max(2_000_000).mime(["image/jpeg", "image/png"]),
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
  profilePhoto: undefined
});

const saving = ref(false);
const profilePhotoDataUrl = ref();

const setProfilePhoto = async (e) => {
  e.preventDefault();
  const { fileName: profilePhotoName, dataURL: profilePhotoDataURL } = await getFile(profileForm.value.values.profilePhoto);
  profilePhotoDataUrl.value = profilePhotoDataURL;
}

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
        <div class="flex justify-between">
          <div>
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
          </div>
          <div class="p-2">
            <img class="m-2 border rounded-lg" :src="profilePhotoDataUrl" width="200px" height="200px" />
            <FormField v-slot="{ componentField }" name="profilePhoto">
              <FormItem>
                <FormLabel>Profile Photo</FormLabel>
                <FormControl>
                  <input
                    id="profilePhoto"
                    type="file"
                    v-bind="componentField"
                    class="relative m-0 block w-full min-w-0 flex-auto cursor-pointer rounded-md border border-solid border-secondary-500 bg-transparent bg-clip-padding px-3 py-[0.32rem] text-base font-normal text-surface transition duration-300 ease-in-out file:-mx-3 file:-my-[0.32rem] file:me-3 file:cursor-pointer file:overflow-hidden file:rounded-none file:border-0 file:border-e file:border-solid file:border-inherit file:bg-transparent file:px-3 file:py-[0.32rem] file:text-surface focus:border-primary focus:text-gray-700 focus:shadow-inset focus:outline-none dark:border-white/70 dark:text-white file:dark:text-white"
                    @change="(e) => setProfilePhoto(e)"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>
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
