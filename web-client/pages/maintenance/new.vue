<template>
  <IContainer>
    <PageTitle title="New Maintenance Request" backTo="/maintenance" />
    <ClientOnly>
        <IForm v-model="schema" :disabled="saving">
            <IRow>
            <IColumn>
                <IFormGroup required>
                <IFormLabel for="description">Description</IFormLabel>
                <ITextarea id="description" name="description" placeholder="Describe your request..." :error="errorTypes" />
                <IFormError for="description" :visible="errorTypes" />
                </IFormGroup>
            </IColumn>
            </IRow>
        </IForm>
    </ClientOnly>
    <div class="bottom">
        <IButton color="primary" :loading="saving" @click="submit()">Submit</IButton>
    </div>
  </IContainer>
</template>
<script setup>
import { useForm } from "@inkline/inkline/composables";

const saving = ref(false);

const { schema, form, validate } = useForm({
    description: { ...fieldOptions },
});

const submit = () => {
    saving.value = true;
    console.log(form.value);
}
</script>
<style scoped>
.bottom {
    margin: 12px;
    display: flex;
    justify-content: center;
}
</style>
