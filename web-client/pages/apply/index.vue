<template>
  <IToast v-if="successfulSubmission" color="success">
    <p>You successfully submitted your application!</p>
  </IToast>
  <div>
    <ClientOnly>
      <IForm v-model="schema">
        <IRow>
          <IColumn>
            <div>
              <h3 class="title">Rental Application</h3>
            </div>
          </IColumn>
        </IRow>
        <div v-if="successfulSubmission" class="form-container">
          <IRow>
            <IColumn>
              <div class="have-an-account">
                <p class="lead">Thank you for submiting an application!</p>
                <p class="lead">You may recieve a request for additional information via email.</p>
                <p>Please close this browser tab.</p>
              </div>
            </IColumn>
          </IRow>
        </div>
        <div v-else class="form-container">
          <IRow>
            <p class="lead">Applicant</p>
          </IRow>
          <IRow>
            <IColumn xs="12">
              <IFormGroup required>
                <IFormLabel for="property">Which property are you applying for?</IFormLabel>
                <IInput v-model="property" id="property" name="property" autocomplete :error="errorTypes" />
                <IFormError for="property" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="12" lg="4">
              <IFormGroup required>
                <IFormLabel for="email">Email address</IFormLabel>
                <IInput id="email" name="email" autocomplete :error="errorTypes">
                  <template #append>
                    <i class="fa-regular fa-envelope"></i>
                  </template>
                </IInput>
                <IFormError for="email" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="6">
              <IFormGroup required>
                <IFormLabel for="fullName">Full name</IFormLabel>
                <IInput id="fullName" name="fullName" autocomplete :error="errorTypes" />
                <IFormError for="fullName" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="2">
              <IFormGroup required>
                <IFormLabel for="phone">Phone number</IFormLabel>
                <IInput id="phone" name="phone" autocomplete :error="errorTypes" />
                <IFormError for="phone" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="12" lg="9">
              <IFormGroup required>
                <IFormLabel for="currentAddress">Current address</IFormLabel>
                <IInput id="currentAddress" name="currentAddress" autocomplete :error="errorTypes" />
                <IFormError for="currentAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="3">
              <IFormGroup required>
                <IFormLabel for="lengthAtCurrentAddress">Length of time</IFormLabel>
                <IInput id="lengthAtCurrentAddress" name="lengthAtCurrentAddress" placeholder="Year(s) / Month(s)" autocomplete :error="errorTypes" />
                <IFormError for="lengthAtCurrentAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="12">
              <IFormGroup required>
                <IFormLabel for="reasonForLeaving">Reason for leaving?</IFormLabel>
                <ITextarea id="reasonForLeaving" name="reasonForLeaving" autocomplete :error="errorTypes" />
                <IFormError for="reasonForLeaving" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Employment</p>
          </IRow>
          <IRow>
            <IColumn xs="12" lg="3">
              <IFormGroup required>
                <IFormLabel for="currentEmployer">Current employer</IFormLabel>
                <IInput id="currentEmployer" name="currentEmployer" autocomplete :error="errorTypes" />
                <IFormError for="currentEmployer" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="3">
              <IFormGroup required>
                <IFormLabel for="positionTitle">Position / Title</IFormLabel>
                <IInput id="positionTitle" name="positionTitle" autocomplete :error="errorTypes" />
                <IFormError for="positionTitle" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="3">
              <IFormGroup required>
                <IFormLabel for="supervisorName">Supervisor name</IFormLabel>
                <IInput id="supervisorName" name="supervisorName" autocomplete :error="errorTypes" />
                <IFormError for="supervisorName" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="3">
              <IFormGroup required>
                <IFormLabel for="supervisorPhone">Supervisor contact</IFormLabel>
                <IInput id="supervisorPhone" name="supervisorPhone" autocomplete :error="errorTypes" />
                <IFormError for="supervisorPhone" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="12" lg="6">
              <IFormGroup required>
                <IFormLabel for="employerAddress">Employer's address</IFormLabel>
                <IInput id="employerAddress" name="employerAddress" autocomplete :error="errorTypes" />
                <IFormError for="employerAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="3">
              <IFormGroup required>
                <IFormLabel for="employmentLength">Length of employment?</IFormLabel>
                <IInput id="employmentLength" name="employmentLength" autocomplete :error="errorTypes" />
                <IFormError for="employmentLength" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="3">
              <IFormGroup required>
                <IFormLabel for="monthlyIncome">Monthly income ($)</IFormLabel>
                <IInput id="monthlyIncome" name="monthlyIncome" autocomplete :error="errorTypes" />
                <IFormError for="monthlyIncome" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Rental History</p>
          </IRow>
          <IRow>
            <IColumn xs="12" lg="5">
              <IFormGroup required>
                <IFormLabel for="previousAddress">Previous address</IFormLabel>
                <IInput id="previousAddress" name="previousAddress" autocomplete :error="errorTypes" />
                <IFormError for="previousAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="3">
              <IFormGroup required>
                <IFormLabel for="previousLandlordName">Landlord's name</IFormLabel>
                <IInput id="previousLandlordName" name="previousLandlordName" autocomplete :error="errorTypes" />
                <IFormError for="previousLandlordName" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="2">
              <IFormGroup required>
                <IFormLabel for="previousLandlordPhone">Landlord's phone</IFormLabel>
                <IInput id="previousLandlordPhone" name="previousLandlordPhone" autocomplete :error="errorTypes" />
                <IFormError for="previousLandlordPhone" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" lg="2">
              <IFormGroup required>
                <IFormLabel for="lengthOfResidence">Length of recidence</IFormLabel>
                <IInput id="lengthOfResidence" name="lengthOfResidence" placeholder="Year(s) / Month(s)" autocomplete :error="errorTypes" />
                <IFormError for="lengthOfResidence" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="12">
              <IFormGroup required>
                <IFormLabel for="reasonForMoving">Reason for moving?</IFormLabel>
                <ITextarea id="reasonForMoving" name="reasonForMoving" autocomplete :error="errorTypes" />
                <IFormError for="reasonForMoving" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">References</p>
          </IRow>
          <IRow>
            <IColumn xs="12">
              <IFormGroup required>
                <IFormLabel for="references">Please list any personal references and their contact information:
                </IFormLabel>
                <ITextarea id="references" name="references" autocomplete :error="errorTypes" />
                <IFormError for="references" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Financial Information</p>
          </IRow>
          <IRow>
            <IColumn xs="12">
              <IFormGroup>
                <IFormLabel for="otherIncome">Do you have other sources of income? If so, please explain:</IFormLabel>
                <ITextarea id="otherIncome" name="otherIncome" autocomplete :error="errorTypes" />
                <IFormError for="otherIncome" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Additional Information</p>
          </IRow>
          <IRow>
            <IColumn xs="12">
              <IFormGroup>
                <IFormLabel for="numberOfOccupants">How many people do you plan to have living in the unit?</IFormLabel>
                <ITextarea id="numberOfOccupants" name="numberOfOccupants" autocomplete :error="errorTypes" />
                <IFormError for="numberOfOccupants" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="12">
              <IFormGroup>
                <IFormLabel for="socialMedia">Please list any social media accounts you may have:</IFormLabel>
                <ITextarea id="socialMedia" name="socialMedia" autocomplete :error="errorTypes" />
                <IFormError for="socialMedia" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="12" sm="3">
              <IFormGroup required>
                <IFormLabel for="doYouSmoke">Do you smoke?</IFormLabel>
                <IRadioGroup id="doYouSmoke" name="doYouSmoke" :options="[
                  { id: 'yes', label: 'Yes' },
                  { id: 'no', label: 'No' },
                ]" />
                <IFormError for="doYouSmoke" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" sm="3">
              <IFormGroup required>
                <IFormLabel for="doYouHavePets">Do you have pets?</IFormLabel>
                <IRadioGroup id="doYouHavePets" name="doYouHavePets" :options="[
                  { id: 'yes', label: 'Yes' },
                  { id: 'no', label: 'No' },
                ]" />
                <IFormError for="doYouHavePets" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="12" sm="3">
              <IFormGroup required>
                <IFormLabel for="haveYouBeenEvicted">Have you ever been evicted?</IFormLabel>
                <IRadioGroup id="haveYouBeenEvicted" name="haveYouBeenEvicted" :options="[
                  { id: 'yes', label: 'Yes' },
                  { id: 'no', label: 'No' },
                ]" />
                <IFormError for="haveYouBeenEvicted" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <div class="create-account">
                <div>
                  <IButton block color="primary" :loading="loading" @click="createAccount"
                    :disabled="!schema.touched || schema.invalid">
                    Apply</IButton>
                </div>
              </div>
            </IColumn>
          </IRow>
        </div>
      </IForm>
    </ClientOnly>
  </div>
</template>
<script setup>
import { useForm } from "@inkline/inkline/composables";

definePageMeta({
  layout: "public",
});

const { schema, form } = useForm({
  email: {
    validators: [
      {
        name: "required",
      },
      {
        name: "custom",
        message: "Please enter a valid email address.",
        validator: emailValidator,
      },
    ],
  },
  fullName: { validators: [{ name: "required" }] },
  property: { validators: [{ name: "required" }] },
  phone: { validators: [{ name: "required" }] },
  currentAddress: { validators: [{ name: "required" }] },
  lengthAtCurrentAddress: { validators: [{ name: "required" }] },
  reasonForLeaving: { validators: [{ name: "required" }] },
  currentEmployer: { validators: [{ name: "required" }] },
  positionTitle: { validators: [{ name: "required" }] },
  supervisorName: { validators: [{ name: "required" }] },
  supervisorPhone: { validators: [{ name: "required" }] },
  employerAddress: { validators: [{ name: "required" }] },
  employmentLength: { validators: [{ name: "required" }] },
  employerAddress: { validators: [{ name: "required" }] },
  monthlyIncome: { validators: [{ name: "required" }] },
  previousAddress: { validators: [{ name: "required" }] },
  previousLandlordName: { validators: [{ name: "required" }] },
  previousLandlordPhone: { validators: [{ name: "required" }] },
  lengthOfResidence: { validators: [{ name: "required" }] },
  reasonForMoving: { validators: [{ name: "required" }] },
  otherIncome: {},
  references: { validators: [{ name: "required" }] },
  numberOfOccupants: { validators: [{ name: "required" }]},
  socialMedia: {},
  doYouSmoke: { validators: [{ name: "required" }] },
  doYouHavePets: { validators: [{ name: "required" }] },
  haveYouBeenEvicted: { validators: [{ name: "required" }] },
});

const errorTypes = ["touched", "invalid"];
const loading = ref(false);
const successfulSubmission = ref(false);

const createAccount = async () => {
  loading.value = true;
  await tenantApplication(form.value)
    .then(() => {
      successfulSubmission.value = true;
    })

  loading.value = false;
};
</script>
<script>
export default {
  name: "Tenant Application",
};
</script>
<style scoped>
.title {
  text-align: center;
  margin-top: 48px;
}

.create-account {
  margin-top: 18px;
  text-align: center;
  margin-bottom: 200px;
}

.have-an-account {
  text-align: center;
}

p {
  margin-top: 22px;
}

.form-container {
  margin: 0 22px;
}

.column {
  margin: 3px 0;
}
</style>
