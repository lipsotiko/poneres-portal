<script setup>
import { z } from "zod/v4";
import { toTypedSchema } from "@vee-validate/zod";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Loader2 } from "lucide-vue-next";
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

const saving = ref(false);

let formSchema = z.object({
  title: z.string(),
  description: z.string(),
  status: z.string(),
  type: z.string(),
  priority: z.string(),
  costEstimate: z.number(),
});

const onSubmit = async (data) => {
  saving.value = true;
  await saveMaintenanceRequest(data);
  navigateTo("/maintenance");
};
</script>
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
  <Form v-slot="{ meta, values, validate }" as="" keep-values :validation-schema="toTypedSchema(formSchema)">
    <form
      @submit="
        (e) => {
          e.preventDefault();
          validate();
          if (meta.valid) {
            onSubmit(values);
          }
        }
      "
    >
      <div class="py-2">
        <FormField v-slot="{ componentField }" name="title">
          <FormItem>
            <FormLabel>Title</FormLabel>
            <FormControl>
              <Input v-bind="componentField" placeholder="Request title..." />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="grid grid-cols-4 gap-4 py-2">
        <div>
          <FormField v-slot="{ componentField }" name="status">
            <FormItem>
              <FormLabel>Status</FormLabel>
              <FormControl>
                <Select v-bind="componentField">
                  <SelectTrigger>
                    <SelectValue placeholder="Choose a status..." />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem
                        v-for="s in ['REQUESTED', 'APPROVED', 'SCHEDULED', 'CANCELLED', 'COMPLETED']"
                        :value="s"
                      >
                        {{ s }}
                      </SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>
        <div>
          <FormField v-slot="{ componentField }" name="priority">
            <FormItem>
              <FormLabel>Priority</FormLabel>
              <FormControl>
                <Select v-bind="componentField">
                  <SelectTrigger>
                    <SelectValue placeholder="Choose a priority..." />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem v-for="p in ['LOW', 'MEDIUM', 'HIGH', 'EMERGENCY']" :value="p">
                        {{ p }}
                      </SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>
        <div>
          <FormField v-slot="{ componentField }" name="type">
            <FormItem>
              <FormLabel>Type</FormLabel>
              <FormControl>
                <Select v-bind="componentField">
                  <SelectTrigger>
                    <SelectValue placeholder="Choose a type..." />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem
                        v-for="t in ['PLUMBING', 'ELECTIRCAL', 'HVAC', 'STRUCTURAL', 'APPLIANCES', 'Other']"
                        :value="t"
                      >
                        {{ t }}
                      </SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>
        <div>
          <FormField v-slot="{ componentField }" name="costEstimate">
            <FormItem>
              <FormLabel>Estimate ($)</FormLabel>
              <FormControl>
                <Input v-bind="componentField" placeholder="Cost estimate..." type="number" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>
      </div>
      <div class="py-2">
        <div>
          <FormField v-slot="{ componentField }" name="description">
            <FormItem>
              <FormLabel>Description</FormLabel>
              <FormControl>
                <Textarea v-bind="componentField" placeholder="Describe your request..." />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>
      </div>
      <div class="flex justify-end m-4">
        <Button color="primary" type="submit" :disabled="saving">
          <Loader2 v-if="saving" class="w-4 h-4 animate-spin" />
          Submit</Button
        >
      </div>
    </form>
  </Form>
</template>
