<template>
  <div>
    <ILayout>

      <ILayoutHeader>
        <p v-if="!pendingUserData" class="auth">
            {{ userData.email }}&nbsp;|&nbsp;<a size="sm" href="/logout">Logout</a>
          </p>
        <INavbar>
          <INavbarBrand to="/">
            Property Pal
          </INavbarBrand>
          <INavbarCollapsible class="_justify-content:flex-end">
            <INav>
              <INavItem to="/properties">Properties</INavItem>
              <INavItem to="/tenants">Tenants</INavItem>
            </INav>
          </INavbarCollapsible>

        </INavbar>

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
  display: flex;
  margin: 10px 12px;
  justify-content: flex-end;
}
</style>
