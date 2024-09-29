<template>
    <IContainer>
        <PageTitle title="Profile" backTo="/" />
        <IContainer class="user-profile-container">
            <ClientOnly>
                <IForm v-model="schema" :disabled="pendingUserInfo">
                    <IRow>
                        <IColumn xs="12">
                            <IFormGroup required>
                                <IFormLabel for="firstName">First name</IFormLabel>
                                <IInput id="firstName" name="firstName" autocomplete :error="errorTypes" />
                                <IFormError for="firstName" :visible="errorTypes" />
                            </IFormGroup>
                        </IColumn>
                    </IRow>
                    <IRow>
                        <IColumn xs="12">
                            <IFormGroup required>
                                <IFormLabel for="lastName">Last name</IFormLabel>
                                <IInput id="lastName" name="lastName" autocomplete :error="errorTypes" />
                                <IFormError for="lastName" :visible="errorTypes" />
                            </IFormGroup>
                        </IColumn>
                    </IRow>
                    <IRow>
                        <IColumn xs="12">
                            <IFormGroup required>
                                <IFormLabel for="email">Email</IFormLabel>
                                <IInput id="email" name="email" autocomplete :error="errorTypes">
                                    <template #suffix v-if="!pendingUserInfo && !userInfo.verified">
                                        <IButton link size="sm" color="info" @click="verifyEmail">verify</IButton>
                                    </template>
                                </IInput>
                                <IFormError for="email" :visible="errorTypes" />
                            </IFormGroup>
                        </IColumn>
                    </IRow>
                    <div class="save-profile">
                        <IButton block color="primary" :loading="loading" @click="saveProfile"
                            :disabled="schema.invalid">
                            Save changes</IButton>
                    </div>
                </IForm>
                <IToast v-if="verifyEmailBanner" v-model="verifyEmailBanner" color="info" dismissible>
                    <p>Please check your email for a verification link.</p>
                </IToast>
            </ClientOnly>
        </IContainer>
    </IContainer>
</template>
<script setup>
import { useForm } from '@inkline/inkline/composables';

const { pending: pendingUserInfo, data: userInfo } = useFetch(
    "/api/user/info",
    {
        lazy: true,
        server: false,
    },
);

const errorTypes = ['touched', 'invalid'];
const loading = ref(false);
const verifyEmailBanner = ref(false);

const { schema } = useForm({
    firstName: {
        validators: [
            {
                name: 'required'
            }
        ]
    },
    lastName: {
        validators: [
            {
                name: 'required'
            }
        ]
    },
    email: {
        validators: [
            {
                name: 'required'
            }, {
                name: 'custom',
                message: 'Please enter a valid email address.',
                validator: emailValidator
            }
        ]
    },
});

watch(pendingUserInfo, () => {
    if (!pendingUserInfo.value) {
        schema.value.firstName.value = userInfo.value.userProfile.firstName;
        schema.value.lastName.value = userInfo.value.userProfile.lastName;
        schema.value.email.value = userInfo.value.userProfile.email;
    }
})

const saveProfile = async () => {
    loading.value = true;
    await saveUserProfile({
        firstName: schema.value.firstName.value,
        lastName: schema.value.lastName.value,
        email: schema.value.email.value
    }).then(() => {
        loading.value = false;
    })
}

const verifyEmail = () => {
    verifyEmailBanner.value = true;
    sendVerificationEmail();
}
</script>
<style scoped>
.user-profile-container {
    max-width: 500px;
}

.save-profile {
    margin: 18px 0;
    text-align: center;
}
</style>
