<template>
  <IToast v-if="successfulUserRegistration" color="success">
    <p>You successfully signed up!</p>
  </IToast>
  <div class="md:flex md:justify-center">
    <Card class="max-w-full md:mt-18 md:w-[500px]">
      <CardHeader>
        <CardTitle class="text-center">Sign up</CardTitle>
      </CardHeader>
      <CardContent>
        <div v-if="successfulUserRegistration">
          <div class="text-center">
            <p class="lead">Log in to view your dashboard!</p>
            <Button @click="goToLoginPage">Log in</Button>
          </div>
        </div>
        <NewUserForm v-else @afterSubmit="() => (successfulUserRegistration = true)" :showAdminRole="false" />
      </CardContent>
    </Card>
  </div>
</template>
<script setup>
import { useRoute } from "vue-router";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

const successfulUserRegistration = ref(false);

const goToLoginPage = () => {
  navigateTo(loginPage, { external: true });
};

definePageMeta({
  layout: "public",
});
</script>
<script>
export default {
  name: "Sign Up",
};
</script>
