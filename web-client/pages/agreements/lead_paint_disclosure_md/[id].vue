<template>
  <Agreement
    pdf-type="LEAD_PAINT_ADDENDUM_MD_V1"
    title="Lead Paint Disclosure (Maryland)"
    :agreement-id="agreementId"
    :schema="metaDataSchema"
    :test-data="testData"
  >
    <IRow>
      <IColumn xs="6">
        <IFormGroup required>
          <IFormLabel for="landlord">Landlord</IFormLabel>
          <IInput id="landlord" name="landlord" :error="errorTypes" />
          <IFormError for="landlord" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
      <IColumn xs="6">
        <IFormGroup required>
          <IFormLabel for="tenant">Tenant</IFormLabel>
          <IInput id="tenant" name="tenant" :error="errorTypes" />
          <IFormError for="tenant" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
    </IRow>
    <IRow>
      <IColumn xs="12">
        <IFormGroup required>
          <IFormLabel for="leasedAddress">Leased address</IFormLabel>
          <IInput id="leasedAddress" name="leasedAddress" :error="errorTypes" />
          <IFormError for="leasedAddress" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
    </IRow>
    <IRow>
      <IColumn xs="4">
        <IFormGroup required>
          <IFormLabel for="leadKnown">Presence of lead paint?</IFormLabel>
          <IRadioGroup
            id="leadKnown"
            name="leadKnown"
            :options="[
              { id: 'yes', label: 'Yes' },
              { id: 'no', label: 'No' },
            ]"
          />
          <IFormError for="leadKnown" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
      <IColumn>
        <IFormGroup>
          <IFormLabel for="explain1"
            >Known lead-based paint and/or lead-based paint hazards are present in the housing (explain)</IFormLabel
          >
          <ITextarea id="explain1" name="explain1" :error="errorTypes" />
          <IFormError for="explain1" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
    </IRow>
    <IRow>
      <IColumn xs="4">
        <IFormGroup required>
          <IFormLabel for="documentsProvided">Records available to the lessor?</IFormLabel>
          <IRadioGroup
            id="documentsProvided"
            name="documentsProvided"
            :options="[
              { id: 'yes', label: 'Yes' },
              { id: 'no', label: 'No' },
            ]"
          />
          <IFormError for="leadKnown" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
      <IColumn>
        <IFormGroup>
          <IFormLabel for="explain2"
            >Lessor has provided the lessee with all available records and reports pertaining to lead-based paint and/or
            lead-based paint hazards in the housing</IFormLabel
          >
          <ITextarea id="explain2" name="explain2" :error="errorTypes" />
          <IFormError for="explain2" :visible="errorTypes" />
        </IFormGroup>
      </IColumn>
    </IRow>
  </Agreement>
</template>
<script setup>
const {
  params: { id: agreementId },
} = useRoute();

const metaDataSchema = {
  landlord: { ...fieldOptions },
  tenant: { ...fieldOptions },
  leasedAddress: { ...fieldOptions },
  leadKnown: {},
  explain1: {},
  documentsProvided: {},
  explain2: {},
};

const testData = {
  landlord: "Evangelos Poneres",
  tenant: "Stephan Michael Nutty, Kristy Diane Nutty",
  leasedAddress: "10722 LANCEWOOD RD, COCKEYSVILLE, MD 21030",
  leadKnown: "no",
  documentsProvided: "no",
  explain1: "Hello",
  explain2: "World",
};
</script>
