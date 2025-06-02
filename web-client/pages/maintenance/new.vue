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
            <BreadcrumbLink href="/maintenance">Maintenance</BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbPage>New Maintenance Request</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
    </template>
  </DefaultLayoutWrapper>
  <ClientOnly>
    <IForm v-model="schema" :disabled="saving">
      <IRow>
        <IColumn>
          <IFormGroup required>
            <IFormLabel for="title">Title</IFormLabel>
            <IInput id="title" name="title" placeholder="Request title..." :error="errorTypes" />
            <IFormError for="title" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
      </IRow>
      <IRow>
        <IColumn>
          <IFormGroup required>
            <IFormLabel for="status">Status</IFormLabel>
            <ISelect
              id="status"
              name="status"
              :options="[
                { id: 'REQUESTED', label: 'REQUESTED' },
                { id: 'APPROVED', label: 'APPROVED' },
                { id: 'SCHEDULED', label: 'SCHEDULED' },
                { id: 'CANCELLED', label: 'CANCELLED' },
                { id: 'COMPLETED', label: 'COMPLETED' },
              ]"
              placeholder="Choose a status.."
              :error="errorTypes"
            />
            <IFormError for="status" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
        <IColumn>
          <IFormGroup required>
            <IFormLabel for="priority">Priority</IFormLabel>
            <ISelect
              id="priority"
              name="priority"
              :options="[
                { id: 'Low', label: 'Low' },
                { id: 'Medium', label: 'Medium' },
                { id: 'High', label: 'High' },
                { id: 'Emergency', label: 'Emergency' },
              ]"
              placeholder="Choose a priority.."
              :error="errorTypes"
            />
            <IFormError for="priority" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
        <IColumn>
          <IFormGroup required>
            <IFormLabel for="type">Type</IFormLabel>
            <ISelect
              id="type"
              name="type"
              :options="[
                { id: 'Plumbing', label: 'Plumbing' },
                { id: 'Electrical', label: 'Electrical' },
                { id: 'HVAC', label: 'HVAC' },
                { id: 'Structural', label: 'Structural' },
                { id: 'Appliances', label: 'Appliances' },
                { id: 'Other', label: 'Other' },
              ]"
              placeholder="Choose a type.."
              :error="errorTypes"
            />
            <IFormError for="type" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
        <IColumn>
          <IFormGroup required>
            <IFormLabel for="costEstimate">Estimate ($)</IFormLabel>
            <IInput id="costEstimate" name="costEstimate" placeholder="Cost estimate..." type="number" :error="errorTypes" />
            <IFormError for="costEstimate" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
      </IRow>
      <IRow>
        <IColumn>
          <IFormGroup required>
            <IFormLabel for="description">Description</IFormLabel>
            <!-- <Textarea /> -->
            <ITextarea id="description" name="description" placeholder="Describe your request..." :error="errorTypes" />
            <IFormError for="description" :visible="errorTypes" />
          </IFormGroup>
        </IColumn>
      </IRow>
    </IForm>
  </ClientOnly>
  <div class="bottom">
    <Button color="primary" @click="submit()" :disabled="saving || loading">
      <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
      Submit</Button
    >
  </div>
</template>
<script setup>
import * as z from "zod";

import { useForm } from "@inkline/inkline/composables";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Textarea } from '@/components/ui/textarea'
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-vue-next";

const saving = ref(false);

const formSchema = [
  z.object({
    title: z.string(),
    description: z.string(),
    status: z.string(),
    type: z.string(),
    priority: z.string(),
    costEstimate: z.number(),
  })
];

const { schema, form, validate } = useForm({
  status: { ...fieldOptions, value: 'REQUESTED' },
  type: { ...fieldOptions },
  priority: { ...fieldOptions },
  costEstimate: { ...fieldOptions },
  title: { ...fieldOptions },
  description: { ...fieldOptions },
});

const submit = async () => {
  saving.value = true;
  await saveMaintenanceRequest(form.value);
  navigateTo("/maintenance");
};
</script>
<style scoped>
.bottom {
  margin: 12px;
  display: flex;
  justify-content: center;
}

textarea[id="description"] {
  height: 222px;
}
</style>
