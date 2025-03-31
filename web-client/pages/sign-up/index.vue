<template>
  <IToast v-if="successfulUserRegistration" color="success">
    <p>You successfully signed up!</p>
  </IToast>
  <IContainer class="sign-up-container">
    <div>
      <h3 class="title">Sign up</h3>
    </div>
    <div v-if="successfulUserRegistration">
      <IRow>
        <IColumn>
          <div class="have-an-account">
            <p class="lead">Log in to view your dashboard!</p>
            <IButton block color="primary" :href="loginPage">Log in</IButton>
          </div>
        </IColumn>
      </IRow>
    </div>
    <NewUserForm v-else @afterSubmit="() => successfulUserRegistration = true" :showAdminRole="false" />
  </IContainer>
</template>
<script setup>
import { useRoute } from 'vue-router'

const successfulUserRegistration = ref(false);

definePageMeta({
  layout: "public",
});
</script>
<script>
export default {
  name: "Sign Up",
};
</script>
<style>
.sign-up-container {
  padding-top: 62px;
  max-width: 500px;

  @media screen and (max-width: 768px) {
    padding-top: 22px;
    max-width: 100%;
  }
}

.title {
  text-align: center;
}
</style>
