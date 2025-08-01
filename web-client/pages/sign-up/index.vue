<template>
  <div class="md:flex md:justify-center">
    <Card class="max-w-full md:mt-18 md:w-[500px]">
      <CardHeader>
        <CardTitle class="text-center">Sign up</CardTitle>
        <CardDescription v-if="successfulUserRegistration" class="text-center">
          Log in to view your dashboard!
        </CardDescription>
      </CardHeader>
      <CardContent v-if="!successfulUserRegistration">
        <NewUserForm
          @afterSubmit="
            () => {
              successfulUserRegistration = true;
              toast.success('You successfully signed up!');
            }
          "
          :showAdminRole="false"
        />
      </CardContent>
      <CardFooter v-else class="inline">
        <div class="text-center">
          <Button @click="goToLoginPage">Log in</Button>
        </div>
      </CardFooter>
    </Card>
  </div>
</template>
<script setup>
import { useRoute } from "vue-router";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { toast } from "vue-sonner";

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
