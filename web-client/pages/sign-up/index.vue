<template>
    <IToast v-if="successfulUserRegistration" color="success">
        <p>You successfully signed up!</p>
    </IToast>
    <IToast v-if="errorMessage" color="warning">
        <p>{{ errorMessage }}</p>
    </IToast>
    <IContainer class="sign-up-container">
        <ClientOnly>
            <IForm v-model="schema">
                <IRow>
                    <IColumn>
                        <div>
                            <h3 class="title">Sign up</h3>
                        </div>
                    </IColumn>
                </IRow>
                <div v-if="successfulUserRegistration">
                    <IRow>
                        <IColumn>
                            <div class="have-an-account">
                                <p class="lead">Log in to view your dashboard!</p>
                                <IButton block color="primary" :href="loginPage">Log in</IButton>
                            </div>
                        </IColumn>
                    </IRow>
                </div>
                <div v-else>
                    <IRow>
                        <IColumn xs="12">
                            <IFormGroup required>
                                <IFormLabel for="email">Email address</IFormLabel>
                                <IInput v-model="email" id="email" name="email" autocomplete :error="errorTypes">
                                    <template #append>
                                        <i class="fa-regular fa-envelope"></i>
                                    </template>
                                </IInput>
                                <IFormError for="email" :visible="errorTypes" />
                            </IFormGroup>
                        </IColumn>
                    </IRow>
                    <IRow>
                        <IColumn xs="6">
                            <IFormGroup required>
                                <IFormLabel for="firstName">First name</IFormLabel>
                                <IInput v-model="firstName" id="firstName" name="firstName" autocomplete
                                    :error="errorTypes" />
                                <IFormError for="firstName" :visible="errorTypes" />
                            </IFormGroup>
                        </IColumn xs="6">
                        <IColumn>
                            <IFormGroup required>
                                <IFormLabel for="lastName">Last name</IFormLabel>
                                <IInput v-model="lastName" id="lastName" name="lastName" autocomplete :error="errorTypes" />
                                <IFormError for="lastName" :visible="errorTypes" />
                            </IFormGroup>
                        </IColumn>
                    </IRow>
                    <IRow>
                        <IColumn>
                            <IFormGroup required>
                                <IFormLabel for="password">Set a password</IFormLabel>
                                <IInput v-model="password" id="password" name="password" type="password" autocomplete
                                    :error="errorTypes" />
                                <IFormError for="password" :visible="errorTypes" />
                            </IFormGroup>
                        </IColumn>
                    </IRow>
                    <IRow>
                        <IColumn>
                            <div class="create-account">
                                <div>
                                    <IButton block color="primary" :loading="loading" @click="createAccount"
                                        :disabled="!schema.touched || schema.invalid">
                                        Create account</IButton>
                                </div>
                            </div>
                        </IColumn>
                    </IRow>
                    <IRow>
                        <IColumn>
                            <div class="have-an-account">
                                <p class="lead">
                                    Already have an account?
                                    <a :href="loginPage">
                                        Sign in
                                    </a>
                                </p>
                            </div>
                        </IColumn>
                    </IRow>
                </div>
            </IForm>
        </ClientOnly>
    </IContainer>
</template>
<script setup>
import { useForm } from '@inkline/inkline/composables';

const loginPage = 'https://poneres-portal-erg0gza3d2hwc6dh.eastus2-01.azurewebsites.net/oauth2/authorization/okta';

definePageMeta({
    layout: 'public'
})

const { schema } = useForm({
    email: {
        validators: [
            {
                name: 'required'
            },
            {
                name: 'custom',
                message: 'Please enter a valid email address.',
                validator: (email) => {
                    if (email === undefined) {
                        return false;
                    }
                    const isEmail = email
                        .toLowerCase()
                        .match(
                            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                        );
                    return isEmail != null;
                }
            }]
    },
    firstName: {
        validators: [{ name: 'required' }]
    },
    lastName: {
        validators: [{ name: 'required' }]
    },
    password: {
        validators: [
            {
                name: 'required'
            },
            {
                name: 'minLength',
                value: 8
            },
            {
                name: 'custom',
                message: 'Password must contain at least one lower case, one upper case, and one special character.',
                validator: (password) => {
                    var isValidPassword = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
                    return isValidPassword.test(password);
                }
            }
        ]
    },
});

const errorTypes = ['touched', 'invalid'];
const email = ref();
const firstName = ref();
const lastName = ref();
const password = ref();
const loading = ref(false);
const errorMessage = ref();
const successfulUserRegistration = ref(false);

const createAccount = async () => {
    loading.value = true;
    await signUpLandlord({
        email: email.value,
        firstName: firstName.value,
        lastName: lastName.value,
        password: password.value
    }).then(() => {
        errorMessage.value = undefined;
        successfulUserRegistration.value = true;
    }).catch(err => {
        errorMessage.value = err.data.message;
    })

    loading.value = false;
}

</script>
<script>
export default {
    name: "Sign Up",
};
</script>
<style scoped>
.sign-up-container {
    padding-top: 62px;
    max-width: 500px;

    @media screen and (max-width: 768px) {
        padding-top: 22px;
        max-width: 100%;
    }
}

.title {
    text-align: center;
}

.create-account {
    margin-top: 18px;
    text-align: center;
}

.have-an-account {
    text-align: center;
}
</style>
