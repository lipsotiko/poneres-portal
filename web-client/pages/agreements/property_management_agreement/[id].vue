<template>
    <Agreement
        pdf-type="PROPERTY_MANAGEMENT_AGREEMENT_V1"
        title="Property Management Agreement"
        :agreement-id="agreementId"
        :schema="metaDataSchema"
        :test-data="testData"
        recipientName-1="Manager name"
        recipientEmail-1="Manager email"
        recipientName-2="Owner name"
        recipientEmail-2="Owner email"
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
              <IFormLabel for="maxExpensesInDollars">Max expenses ($)</IFormLabel>
              <IInput id="maxExpensesInDollars" name="maxExpensesInDollars" type="number" :error="errorTypes" />
              <IFormError for="maxExpensesInDollars" :visible="errorTypes" />
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
          <IColumn xs="3">
            <IFormGroup required>
              <IFormLabel for="termStartDate">Start date</IFormLabel>
              <IInput id="termStartDate" name="termStartDate" type="month" :error="errorTypes" />
              <IFormError for="termStartDate" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn xs="3">
            <IFormGroup required>
              <IFormLabel for="termEndDate">End date</IFormLabel>
              <IInput id="termEndDate" name="termEndDate" type="month" :error="errorTypes" />
              <IFormError for="termEndDate" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
        </IRow>
        <IRow>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="ownerAddress">Owner Notice Address</IFormLabel>
              <IInput id="ownerAddress" name="ownerAddress" :error="errorTypes" />
              <IFormError for="ownerAddress" :visible="errorTypes" />
            </IFormGroup>
          </IColumn>
          <IColumn>
            <IFormGroup required>
              <IFormLabel for="managerAddress">Manager Notice Address</IFormLabel>
              <IInput id="managerAddress" name="managerAddress" :error="errorTypes" />
              <IFormError for="managerAddress" :visible="errorTypes" />
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
  maxExpensesInDollars: { ...fieldOptions },
  compensationPercentage: { ...fieldOptions },
  noticeDays: { ...fieldOptions },
  addendum: {},
  termStartDate: { ...fieldOptions },
  termEndDate: { ...fieldOptions },
  ownerAddress: { ...fieldOptions },
  managerAddress: { ...fieldOptions },
};

const testData = {
  owner: "Hello",
  manager: "World",
  propertyAddress: "123 Cool Ln, Baltimore, MD 22111",
  maxExpensesInDollars: 2000,
  compensationPercentage: 10,
  noticeDays: 60,
  addendum: "Test",
  termStartDate: dayjs().add(-2, "month").format("YYYY-MM"),
  termEndDate: dayjs().add(2, "month").format("YYYY-MM"),
  ownerAddress: "222 Cool Ln, Baltimore, MD 22111",
  managerAddress: "333 Cool Ln, Baltimore, MD 22111"
};
</script>
