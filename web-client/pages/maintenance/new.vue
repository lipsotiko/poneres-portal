<template>
  <IContainer>
    <PageTitle title="New Maintenance Request" backTo="/maintenance" />
    <ClientOnly>
        <IForm v-model="schema" :disabled="saving">
            <IRow>
                <IColumn>
                    <IFormGroup required>
                    <IFormLabel for="priority">Priority</IFormLabel>
                    <ISelect id="priority" name="priority" :options="[
                        { id: 'Low', label: 'Low' },
                        { id: 'Medium', label: 'Medium' },
                        { id: 'High', label: 'High' },
                        { id: 'Emergency', label: 'Emergency' },
                    ]" placeholder="Choose a priority.." :error="errorTypes" />
                    <IFormError for="priority" :visible="errorTypes" />
                    </IFormGroup>
                </IColumn>
                <IColumn>
                    <IFormGroup required>
                    <IFormLabel for="type">Type</IFormLabel>
                    <ISelect id="type" name="type" :options="[
                        { id: 'Plumbing', label: 'Plumbing' },
                        { id: 'Electrical', label: 'Electrical' },
                        { id: 'HVAC', label: 'HVAC' },
                        { id: 'Structural', label: 'Structural' },
                        { id: 'Appliances', label: 'Appliances' },
                        { id: 'Other', label: 'Other' },
                    ]" placeholder="Choose a type.." :error="errorTypes" />
                    <IFormError for="type" :visible="errorTypes" />
                    </IFormGroup>
                </IColumn>
            </IRow>
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
        <IButton color="primary" :loading="saving" :disabled="saving" @click="submit()">Submit</IButton>
    </div>
  </IContainer>
</template>
<script setup>
import { useForm } from "@inkline/inkline/composables";

const saving = ref(false);

const { schema, form, validate } = useForm({
    type: { ...fieldOptions },
    priority: { ...fieldOptions },
    description: { ...fieldOptions },
});

const submit = async () => {
    saving.value = true;
    await saveMaintenanceRequest(form.value)
    navigateTo("/maintenance");
}
</script>
<style scoped>
.bottom {
    margin: 12px;
    display: flex;
    justify-content: center;
}

textarea[id="description"] {
    height: 222px;
}
</style>
