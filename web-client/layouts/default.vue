<template>
  <div>
    <ILayout @keydown.esc="() => (open = false)">
      <IToast v-if="errorMessage" color="warning" position="bottom" dismissible>
        <p>{{ errorMessage }}</p>
      </IToast>
      <ILayoutHeader>
        <IDropdown class="_display:flex _justify-content:flex-end">
          <IButton circle size="md" class="profile">
            <template #icon>
              <i class="fa-regular fa-user"></i>
            </template>
          </IButton>
          <template #body>
            <IDropdownItem to="/profile">Profile</IDropdownItem>
            <!-- <IDropdownItem to="/earnings">Earnings</IDropdownItem> -->
            <IDropdownDivider v-if="isAdmin" />
            <IDropdownItem v-if="isAdmin" to="/admin">Admin</IDropdownItem>
            <IDropdownDivider />
            <IDropdownItem href="/logout">
              <span class="_color:danger!">Sign out</span>
            </IDropdownItem>
          </template>
        </IDropdown>
        <INavbar>
          <INavbarBrand to="/">
            <strong>Poneres.com</strong>
          </INavbarBrand>
          <INavbarCollapsible class="_justify-content:flex-end">
            <INav>
              <INavItem to="/agreements">Agreements</INavItem>
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
import { provide } from "vue";
import "ag-grid-community/styles/ag-grid.css"; // Mandatory CSS required by the Data Grid
const { isAdmin } = useAuth();
const errorMessage = ref();
provide("errorMessage", errorMessage);
const open = ref(false);
provide("sideBarOpen", open);
</script>
<style scoped>
.profile {
  margin: 10px 12px;
}
</style>
