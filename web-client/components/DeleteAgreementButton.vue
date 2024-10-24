<template>
  <IButton color="danger" @click="deleteModalVisible = true" :disabled="isNew" :loading="deleting"> Delete </IButton>
  <IModal v-model="deleteModalVisible">
    <template #header>Delete agreement?</template>
    <span>Are you sure you want to delete this agreement?</span>
    <template #footer>
      <div style="display: flex; justify-content: space-between">
        <IButton outline color="dark" @click="deleteModalVisible = false">No, keep it.</IButton>
        <IButton color="danger" @click="handleDelete()" :loading="deleting">Yes, delete!</IButton>
      </div>
    </template>
  </IModal>
</template>
<script setup>
import { inject } from "vue";
const errorMessage = inject("errorMessage");
const deleting = ref(false);
const deleteModalVisible = ref(false);
const { id, isNew } = defineProps(["id", "isNew"]);

const handleDelete = () => {
  deleting.value = true;
  deleteAgreement(id)
    .then(() => {
      navigateTo("/agreements");
    })
    .catch((err) => {
      errorMessage.value = err.data.message;
      deleteModalVisible.value = false;
      deleting.value = false;
    });
};
</script>
