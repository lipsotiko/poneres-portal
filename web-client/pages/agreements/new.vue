<template>
  <IContainer>
    <PageTitle title="New Agreement" backTo="/agreements" />
    <div>
      <ISelect
        v-model="selectedAgreementPath"
        :options="agreementTypes"
        @update:modelValue="handleSelection()"
      />
    </div>
  </IContainer>
</template>
<script setup>
const router = useRouter();

const agreementTypes = [
  {
    id: "/agreements/lease_agreement_md/New",
    label: "Residential Dwelling Lease (Maryland)",
  },
];
const selectedAgreementPath = ref();

const handleSelection = () => {
  router.push(selectedAgreementPath.value);
};
</script>
