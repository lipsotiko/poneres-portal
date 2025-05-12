<template>
  <DefaultLayoutWrapper>
    <template #breadcrumbs>
      <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbPage>Home</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <div class="top">
    <p v-if="auth?.roles.includes('RESIDENT')" class="lead">Welcome to your resident portal!</p>
    <p v-else-if="auth?.roles.includes('OWNER')" class="lead">Welcome to your owner portal!</p>
    <p v-else-if="auth?.roles.includes('FACILITY')" class="lead">Welcome to your facility portal!</p>
    <p v-else-if="auth?.roles.includes('PROVIDER')" class="lead">Welcome to your provider portal!</p>
    <p v-else-if="auth?.roles.includes('ADMIN')" class="lead">Welcome to your admin portal!</p>
    <p v-else class="lead">
      <Loader2 class="w-4 h-4 animate-spin" />
    </p>
  </div>
</template>
<script setup>
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Loader2 } from "lucide-vue-next";

const auth = useState("auth");
</script>
