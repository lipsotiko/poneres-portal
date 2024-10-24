<template>
  <IRow>
    <p class="lead">Recipients</p>
  </IRow>
  <IForm v-model="recipientsSchema">
    <IRow v-for="(_, index) in recipientsSchema.recipients" :key="index">
      <IColumn xs="6">
        <IFormGroup required>
          <IFormLabel :for="`recipients.${index}.name`">
            <span v-if="index === 0">Landlord name</span>
            <span v-else>Tenant name {{ index }}</span>
          </IFormLabel>
          <IInput :name="`recipients.${index}.name`" :error="errorTypes" />
          <IFormError :for="`recipients.${index}.name`" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
      <IColumn xs="6">
        <IFormGroup required>
          <IFormLabel :for="`recipients.${index}.email`">
            <span v-if="index === 0">Landlord email</span>
            <span v-else>Tenant email {{ index }}</span>
          </IFormLabel>
          <IInput :name="`recipients.${index}.email`" :error="errorTypes">
            <template #append>
              <IButton v-if="index > 1" size="sm" @click="removeRecipient(index)">
                <i class="fa-solid fa-trash"></i>
              </IButton>
            </template>
          </IInput>
          <IFormError :for="`recipients.${index}.email`" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
    </IRow>
  </IForm>
</template>
<script setup>
const recipientsSchema = defineModel();

const removeRecipient = (index) => {
  if (recipientsSchema.value.recipients.length === 1) {
    return;
  }
  recipientsSchema.value.recipients.splice(index, 1);
};
</script>
