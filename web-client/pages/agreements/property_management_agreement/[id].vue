<template>
    <Agreement
        pdf-type="PROPERTY_MANAGEMENT_AGREEMENT_V1"
        title="Property Management Agreement"
        :agreement-id="agreementId"
        :schema="metaDataSchema"
        :test-data="testData"
        >
    </Agreement>
</template>
<script setup>
const {
  params: { id: agreementId },
} = useRoute();

const metaDataSchema = {};
const testData = {};
</script>
