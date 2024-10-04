<template>
  <IContainer>
    <PageTitle title="User" back-to="/admin" />
    {{ userId }}
  </IContainer>
</template>
<script setup>
const {
  params: { id: userId },
} = useRoute();
</script>
