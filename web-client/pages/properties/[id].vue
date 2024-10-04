<template>
  <IContainer>
    <PageTitle title="Property" back-to="/properties" />
    {{ propertyId }}
  </IContainer>
</template>
<script setup>
const {
  params: { id: propertyId },
} = useRoute();
</script>
