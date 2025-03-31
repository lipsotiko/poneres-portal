<template>
    <IContainer>
        <PageTitle title="New User" backTo="/admin" />
        <NewUserForm @afterSubmit="afterSubmit" :showAdminRole="true" />
    </IContainer>
</template>
<script setup>

const afterSubmit = () => {
    navigateTo("/admin");
}
</script>
