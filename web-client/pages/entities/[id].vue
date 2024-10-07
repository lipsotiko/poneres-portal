<template>
  <IContainer>
    <PageTitle title="Entity" back-to="/entities" />
    {{ entityId }}
  </IContainer>
</template>
<script setup>
const {
  params: { id: entityId },
} = useRoute();
</script>
