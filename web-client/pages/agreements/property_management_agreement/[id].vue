<template>
    <Agreement
        pdf-type="PROPERTY_MANAGEMENT_AGREEMENT_V1"
        title="Property Management Agreement"
        :agreement-id="agreementId"
        :schema="metaDataSchema"
        :test-data="testData"
        >
        <IRow>
          <IColumn xs="3">
            <IFormGroup required>
              <IFormLabel for="agreementDate">Agreement date</IFormLabel>
              <IInput id="agreementDate" name="agreementDate" type="date" :error="errorTypes" />
              <IFormError for="agreementDate" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn xs="3">
            <IFormGroup required>
              <IFormLabel for="maxExpenses">Max expenses ($)</IFormLabel>
              <IInput id="maxExpenses" name="maxExpenses" type="number" :error="errorTypes" />
              <IFormError for="maxExpenses" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn xs="3">
            <IFormGroup required>
              <IFormLabel for="compensationPercentage">Compensation (%)</IFormLabel>
              <IInput id="compensationPercentage" name="compensationPercentage" type="number" :error="errorTypes" />
              <IFormError for="compensationPercentage" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn xs="3">
            <IFormGroup required>
              <IFormLabel for="noticeDays">Termination notice days</IFormLabel>
              <IInput id="noticeDays" name="noticeDays" type="number" :error="errorTypes" />
              <IFormError for="noticeDays" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="owner">Owner</IFormLabel>
              <IInput id="owner" name="owner" :error="errorTypes" />
              <IFormError for="owner" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="manager">Manager</IFormLabel>
              <IInput id="manager" name="manager" :error="errorTypes" />
              <IFormError for="manager" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="propertyAddress">Property address</IFormLabel>
              <IInput id="propertyAddress" name="propertyAddress" :error="errorTypes" />
              <IFormError for="propertyAddress" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="totalPercentageOfRent">Initial Term Rent Compensation (%)</IFormLabel>
              <IInput id="totalPercentageOfRent" name="totalPercentageOfRent" type="number" :error="errorTypes" />
              <IFormError for="totalPercentageOfRent" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="totalYearsOfRent">Rent compensation term years</IFormLabel>
              <IInput id="totalYearsOfRent" name="totalYearsOfRent" type="number" :error="errorTypes" />
              <IFormError for="totalYearsOfRent" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup>
              <IFormLabel for="remainderPercentageOfRent">Term Remainder Rent Compensation (%)</IFormLabel>
              <IInput id="remainderPercentageOfRent" name="remainderPercentageOfRent" type="number" :error="errorTypes" />
              <IFormError for="remainderPercentageOfRent" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <IFormGroup>
              <IFormLabel for="totalPercentageOfRentExt">Total percentage of extension (%)</IFormLabel>
              <IInput id="totalPercentageOfRentExt" name="totalPercentageOfRentExt" type="number" :error="errorTypes" />
              <IFormError for="totalPercentageOfRentExt" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup>
              <IFormLabel for="totalDollarsOfRentExt">Total dollars of extension ($)</IFormLabel>
              <IInput id="totalDollarsOfRentExt" name="totalDollarsOfRentExt" type="number" :error="errorTypes" />
              <IFormError for="totalDollarsOfRentExt" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="addendum">Addendum</IFormLabel>
              <ITextarea id="addendum" name="addendum" :error="errorTypes" />
              <IFormError for="addendum" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
    </Agreement>
</template>
<script setup>
const {
  params: { id: agreementId },
} = useRoute();
const dayjs = useDayjs();

const metaDataSchema = {
  agreementDate: { ...fieldOptions, value: dayjs().format("YYYY-MM-DD") },
  owner: { ...fieldOptions },
  manager: { ...fieldOptions },
  propertyAddress: { ...fieldOptions },
  maxExpenses: { ...fieldOptions },
  compensationPercentage: { ...fieldOptions },
  noticeDays: { ...fieldOptions },
  totalPercentageOfRent: { ...fieldOptions, value: 10 },
  totalYearsOfRent: { ...fieldOptions, value: 1 },
  remainderPercentageOfRent: { value: 0 },
  totalPercentageOfRentExt: { value: 0 },
  totalDollarsOfRentExt: { value: 0 },
  addendum: {}
};

const testData = {
  owner: "Hello",
  manager: "World",
  propertyAddress: "123 Cool Ln, Baltimore, MD 22111",
  maxExpenses: 2000,
  compensationPercentage: 10,
  noticeDays: 60,
  addendum: "Test",
  totalPercentageOfRent: 10,
  totalYearsOfRent: 1,
  remainderPercentageOfRent: 0,
  totalPercentageOfRentExt: 0,
  totalDollarsOfRentExt: 0,
};
</script>
