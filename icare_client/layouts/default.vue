<template>
  <div>
    <ILayout>
      <ILayoutHeader>
        <p v-if="!pendingUserData" class="auth">
          {{ userData.email }} | <a size="sm" href="/logout">Logout</a>
        </p>
      </ILayoutHeader>
      <ILayoutContent>
        <slot />
      </ILayoutContent>
      <ILayoutFooter> </ILayoutFooter>
    </ILayout>
  </div>
</template>
<script setup>
const { pending: pendingUserData, data: userData } = useFetch(
  "/api/user/info",
  {
    lazy: true,
    server: false,
  },
);
</script>
<script>
export default {
  methods: {
    logout() {
      console.log("logout");
    },
  },
};
</script>
<style>
.auth {
  margin: 22px;
  float: right;
}
</style>
