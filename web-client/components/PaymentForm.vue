<script setup>
import { Button } from "@/components/ui/button";
import { usePaymentIntent, VueStripeProvider, VueStripeElements, VueStripePaymentElement } from "@vue-stripe/vue-stripe";

const { clientSecret } = defineProps(["clientSecret"]);
const paymentMethodComplete = ref(false);
const stripeInstance = ref("");
const elementsInstance = ref();

let confirmPayment;

const onStripeLoad = (stripe) => {
  console.log('onStripeLoad');
  stripeInstance.value = stripe
}

const onElementsReady = (elements) => {
  console.log('onElementsReady');
  elementsInstance.value = elements
}

// onMounted(async () => {
//   const paymentIntent = usePaymentIntent();
//   confirmPayment = paymentIntent.confirmPayment;
// });

const onChange = (e) => {
  paymentMethodComplete.value = e.complete;
};

const handleSubmit = async () => {
  if (!stripeInstance.value || !elementsInstance.value) return

  const { error } = await stripeInstance.value.confirmPayment({
    elements: elementsInstance.value,
    confirmParams: {
      return_url: window.location.href
    }
  })

  if (error) {
    console.error(error.message)
  }
};
</script>

<template>
  <VueStripeProvider :publishable-key="$config.public.stripePublishableKey" @load="onStripeLoad">
    <VueStripeElements v-if="clientSecret" :client-secret="clientSecret" @ready="onElementsReady">
      <VueStripePaymentElement ref="paymentElement" @change="onChange" />
    </VueStripeElements>
    <Button @click="handleSubmit" :disabled="!paymentMethodComplete">Pay</Button>
  </VueStripeProvider>
</template>
