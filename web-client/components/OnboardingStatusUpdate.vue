<template>
  <Select :defaultValue="value" @update:modelValue="onChange">
    <SelectTrigger class="w-[180px]">
      <SelectValue placeholder="Select a status" />
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectItem value="SUBMITTED"> SUBMITTED </SelectItem>
        <SelectItem value="UNDER_REVIEW"> UNDER REVIEW </SelectItem>
        <SelectItem value="COMPLETE"> COMPLETE </SelectItem>
      </SelectGroup>
    </SelectContent>
  </Select>
</template>
<script setup lang="ts">
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

const props = defineProps(["params"]);
const {
  params: {
    colDef: { context },
    data: { id },
    value,
  },
} = props;

const onChange = (v) => {
  $fetch(`/api/onboarding/update-status/${id}?status=${v}`, {
    method: "POST",
  });
};
</script>
