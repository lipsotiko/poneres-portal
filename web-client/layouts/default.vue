<template>
  <SidebarProvider>
    <Sidebar collapsible="icon">
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Poneres</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu class="pl-0!">
              <SidebarMenuItem
                v-for="item in itemsForAll"
                :key="item.title"
              >
                <SidebarMenuButton asChild>
                  <NuxtLink :href="item.url">
                    <component :is="item.icon" />
                    <span>{{ item.title }}</span>
                  </NuxtLink>
                </SidebarMenuButton>
              </SidebarMenuItem>
              <SidebarMenuItem
                v-if="user?.roles.includes('ADMIN') && $config.public.deploymentType !== 'local'"
                v-for="item in itemsForAdmin"
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
      <div class="m-[10px]">
        <!-- TODO: Remove margin once inkline in removed -->
        <slot />
      </div>
    </main>
  </SidebarProvider>
</template>
<script setup>
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import {
  CalendarDays,
  DollarSign,
  FilePenLine,
  Home,
  Hospital,
  IdCard,
  LogOut,
  Mail,
  MonitorCog,
  Search,
  Settings,
  User,
  View,
} from "lucide-vue-next";
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";
import { Loader2 } from "lucide-vue-next";
import "ag-grid-community/styles/ag-grid.css"; // Mandatory CSS required by the Data Grid

const user = ref();
const open = ref(false);

const { pending: pendingUserInfo } = await useFetch("/api/user/info", {
  lazy: true,
  server: false,
  onResponse({ request, response, options }) {
    // if (response._data.userProfile.roles.includes('PROVIDER')) {
    //   navigateTo("/onboarding");
    // }

    user.value = response._data.userProfile;
  },
});

const itemsForAll = [
  {
    title: "Home",
    url: "/",
    icon: Home,
  },
  {
    title: "Shift Passport",
    url: "/onboarding",
    icon: IdCard,
  },
  {
    title: "Marketplace",
    url: "/",
    icon: Hospital,
  },
  {
    title: "Messages",
    url: "/",
    icon: Mail,
  },
  {
    title: "Calendar",
    url: "/",
    icon: CalendarDays,
  },
  {
    title: "Earnings",
    url: "/earnings",
    icon: DollarSign,
  },
  {
    title: "Profile",
    url: "/profile",
    icon: User,
  },
];

const itemsForAdmin = [
  {
    title: "Admin",
    url: "/admin",
    icon: MonitorCog,
  },
  {
    title: "Agreements",
    url: "/agreements",
    icon: FilePenLine,
  },
  {
    title: "Onboarding Review",
    url: "/onboarding-review",
    icon: View,
  },
];
</script>
