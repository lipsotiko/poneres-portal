<template>
  <DefaultLayoutWrapper>
    <template #breadcrumbs>
      <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbLink href="/"> Home </BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbLink href="/agreements">Agreements</BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbPage>New Agreement</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <Select v-model="selectedAgreementPath" @update:modelValue="(e) => handleSelection(e)">
    <SelectTrigger>
      <SelectValue placeholder="Select an agreement" />
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectItem v-for="o in agreementTypes" :value="o.id"> {{ o.label }} </SelectItem>
      </SelectGroup>
    </SelectContent>
  </Select>
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
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

const router = useRouter();
const selectedAgreementPath = ref();

const handleSelection = (e) => {
  router.push(selectedAgreementPath.value + "New");
};
</script>
