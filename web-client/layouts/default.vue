<template>
  <SidebarProvider>
    <Sidebar>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Poneres</SidebarGroupLabel>
          <SidebarGroupContent>
            <div v-if="pendingUserInfo" class="flex justify-center mt-4">
              <Loader2 class="w-4 h-4 animate-spin" />
            </div>
            <SidebarMenu v-else>
              <SidebarMenuItem
                v-for="item in items.filter((i) => i.showFor == 'ALL' || i.showFor.some((r) => user?.roles.includes(r)))"
                :key="item.title"
              >
                <SidebarMenuButton asChild>
                  <NuxtLink :href="item.url">
                    <component :is="item.icon" />
                    <span>{{ item.title }}</span>
                  </NuxtLink>
                </SidebarMenuButton>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <SidebarMenuButton asChild>
                  <a href="/logout">
                    <component :is="LogOut" />
                    <span>Logout</span>
                  </a>
                </SidebarMenuButton>
              </SidebarMenuItem>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
    <main class="w-full">
      <IToast v-if="errorMessage" color="warning" dismissible>
        <p>{{ errorMessage }}</p>
      </IToast>
      <div class="m-4">
        <slot />
      </div>
    </main>
  </SidebarProvider>
</template>
<script setup>
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { Calendar, Home, Inbox, Search, Settings, User, FilePenLine, Wrench, MonitorCog, LogOut } from "lucide-vue-next";
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";
import { Loader2 } from "lucide-vue-next";
import { provide } from "vue";
import "ag-grid-community/styles/ag-grid.css"; // Mandatory CSS required by the Data Grid
const user = ref();
const errorMessage = ref();
provide("errorMessage", errorMessage);
const open = ref(false);
provide("sideBarOpen", open);

const { pending: pendingUserInfo } = await useFetch("/api/user/info", {
  lazy: true,
  server: false,
  onResponse({ request, response, options }) {
    user.value = response._data.userProfile
  },
});

const items = [
  {
    title: "Home",
    url: "/",
    icon: Home,
    showFor: ["ALL"],
  },
  {
    title: "Inbox",
    url: "#",
    icon: Inbox,
    showFor: ["ALL"],
  },
  {
    title: "Calendar",
    url: "#",
    icon: Calendar,
    showFor: ["ADMIN", "PROVIDER", "FACILITY"],
  },
  {
    title: "Agreements",
    url: "/agreements",
    icon: FilePenLine,
    showFor: ["ADMIN"],
  },
  {
    title: "Maintenance",
    url: "/maintenance",
    icon: Wrench,
    showFor: ["ADMIN"],
  },
  {
    title: "Settings",
    url: "#",
    icon: Settings,
    showFor: ["ALL"],
  },
  {
    title: "Profile",
    url: "/profile",
    icon: User,
    showFor: ["ALL"],
  },
  {
    title: "Admin",
    url: "/admin",
    icon: MonitorCog,
    showFor: ["ADMIN"],
  }
];
</script>

<!-- <div>
    <ILayout @keydown.esc="() => (open = false)">

      <ILayoutHeader>
        <IDropdown class="_display:flex _justify-content:flex-end">
          <IButton circle size="md" class="profile">
            <template #icon>
              <i class="fa-regular fa-user"></i>
            </template>
          </IButton>
          <template #body>
            <IDropdownItem to="/profile">Profile</IDropdownItem>
            <IDropdownItem to="/earnings">Earnings</IDropdownItem>
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
              <INavItem v-if="isAdmin" to="/agreements">Agreements</INavItem>
              <INavItem v-if="isResident" to="/maintenance">Maintenance</INavItem>
            </INav>
          </INavbarCollapsible>
        </INavbar>
      </ILayoutHeader>
      <slot />
    </ILayout>
  </div>
-->
