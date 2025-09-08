<script setup lang="ts">
import { BookUser, Check, Eye, IdCard } from "lucide-vue-next"
import { Button } from "@/components/ui/button";
import { Stepper, StepperDescription, StepperIndicator, StepperItem, StepperTitle, StepperTrigger } from "@/components/ui/stepper"

const steps = [{
  step: 1,
  title: "Onboarding",
  description: "Click here to provider your credentials.",
  icon: IdCard,
}, {
  step: 2,
  title: "Under Review",
  description: "Your credentials are being reviewed.",
  icon: Eye,
}, {
  step: 3,
  title: "Fully Credentialed",
  description: "Your credentialshave been approved.",
  icon: Check,
}]

const stepIndex = ref(1)

</script>

<template>
  <Stepper v-model="stepIndex">
    <StepperItem
      v-for="item in steps"
      :key="item.step"
      class="basis-1/4"
      :step="item.step"
    >
      <StepperTrigger disabled="true">
        <StepperIndicator>
          <component :is="item.icon" class="w-4 h-4" />
        </StepperIndicator>
        <div class="flex flex-col">
          <StepperTitle>
            {{ item.title }}
          </StepperTitle>
          <StepperDescription v-if="item.step === 1">
            <Button size="xs" class="w-full" :disabled="stepIndex !== 1">
                <NuxtLink to="/onboarding">Start</NuxtLink>
            </Button>
          </StepperDescription>
          <StepperDescription v-else>
            {{ item.description }}
          </StepperDescription>
        </div>
      </StepperTrigger>
    </StepperItem>
  </Stepper>
</template>
