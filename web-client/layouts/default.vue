<template>
  <SidebarProvider>
    <Sidebar collapsible="icon">
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel class="border-b-1 h-10 ">
            <div class="flex">
              <img src="/logo.png" class="w-[32px] h-[32px]"/>
              <p class="inline pl-2 py-1 text-lg font-semibold">Boring Resume</p>
            </div>
          </SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu class="pl-0! mt-2">
              <SidebarMenuItem>
                <SidebarMenuButton asChild>
                  <NuxtLink href="/">
                    <component :is="Home" />
                    <span>Home</span>
                  </NuxtLink>
                </SidebarMenuButton>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <SidebarMenuButton asChild>
                  <NuxtLink href="/account">
                    <component :is="User" />
                    <span>Account</span>
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
  Home,
  LogOut,
  MonitorCog,
  User,
} from "lucide-vue-next";
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
import "ag-grid-community/styles/ag-grid.css"; // Mandatory CSS required by the Data Grid

const open = ref(false);
</script>
