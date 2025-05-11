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
    <p v-if="isResident" class="lead">
      Welcome to your resident portal!
    </p>
    <p v-else-if="isOwner" class="lead">
      Welcome to your owner portal!
    </p>
    <p v-else-if="isFacility" class="lead">
      Welcome to your facility portal!
    </p>
    <p v-else-if="isProvider" class="lead">
      Welcome to your provider portal!
    </p>
    <p v-else-if="isAdmin" class="lead">
      Welcome to your admin portal!
    </p>
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
import { Loader2 } from 'lucide-vue-next'

const { isAdmin, isResident, isOwner, isProvider, isFacility } = await useAuth();
</script>
