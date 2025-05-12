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
  <ClientOnly>
    <IForm v-model="schema" :disabled="pendingUserInfo" class="w-lg">
      <IRow>
        <IColumn xs="12">
          <IFormGroup required>
            <IFormLabel for="firstName">First name</IFormLabel>
            <IInput id="firstName" name="firstName" autocomplete :error="errorTypes" />
            <IFormError for="firstName" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
      </IRow>
      <IRow>
        <IColumn xs="12">
          <IFormGroup required>
            <IFormLabel for="lastName">Last name</IFormLabel>
            <IInput id="lastName" name="lastName" autocomplete :error="errorTypes" />
            <IFormError for="lastName" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
      </IRow>
      <IRow>
        <IColumn xs="12">
          <IFormGroup required>
            <IFormLabel for="email">Email</IFormLabel>
            <IInput id="email" name="email" autocomplete :error="errorTypes">
              <template #suffix v-if="!pendingUserInfo && !userInfo.verified">
                <IButton link size="sm" color="info" @click="verifyEmail">verify</IButton>
              </template>
            </IInput>
            <IFormError for="email" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
      </IRow>
      <div class="save-profile">
        <Button block color="primary" @click="saveProfile" :disabled="schema.invalid || loading">
          <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
          Save changes</Button
        >
      </div>
    </IForm>
    <IToast v-if="verifyEmailBanner" v-model="verifyEmailBanner" color="info" class="w-lg" dismissible>
      <p>Please check your email for a verification link.</p>
    </IToast>
    <template #fallback>
      <!-- this will be rendered on server side -->
      <div class="flex justify-center">
        <Loader2 class="w-6 h-6 animate-spin" />
      </div>
    </template>
  </ClientOnly>
</template>
<script setup>
import { useForm } from "@inkline/inkline/composables";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-vue-next";

const { pending: pendingUserInfo, data: userInfo } = await useFetch("/api/user/info", {
  lazy: true,
  server: false,
  onResponse({ request, response, options }) {
    schema.value.firstName.value = response._data.userProfile.firstName;
    schema.value.lastName.value = response._data.userProfile.lastName;
    schema.value.email.value = response._data.userProfile.email;
  },
});

const errorTypes = ["touched", "invalid"];
const loading = ref(false);
const verifyEmailBanner = ref(false);

const { schema } = useForm({
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
});

const saveProfile = async () => {
  loading.value = true;
  await saveUserProfile(userInfo.value.userProfile.id, {
    firstName: schema.value.firstName.value,
    lastName: schema.value.lastName.value,
    email: schema.value.email.value,
  }).then(() => {
    loading.value = false;
  });
};

const verifyEmail = () => {
  verifyEmailBanner.value = true;
  sendVerificationEmail();
};
</script>
<style scoped>
.user-profile-container {
  max-width: 500px;
}

.save-profile {
  margin: 18px 0;
  text-align: center;
}
</style>
