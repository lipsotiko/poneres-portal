<template>
  <IButton link color="dark" :to="path() + params.data.id">
    {{ params.value }}
  </IButton>
</template>
<script setup>
const props = defineProps(["params"]);
const {
  params: {
    colDef: { context },
    data: { type },
  },
} = props;

const path = () => {
  if (type === "LEAD_PAINT_ADDENDUM_MD_V1") {
    return "/agreements/lead_paint_disclosure_md/";
  } else if (type === "LEASE_AGREEMENT_MD_V1") {
    return "/agreements/lease_agreement_md/";
  }
};
</script>
