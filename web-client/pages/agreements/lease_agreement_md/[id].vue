<template>
  <ILayout @keydown.esc="() => (open = false)">
    <PdfPreview :key="metadataForm" :metadata="metadataForm" :type="pdfType" />
    <IContainer>
      <PageTitle :title="`Residential Dwelling Lease (Maryland) - ${agreementId}`" backTo="/agreements" />
      <ClientOnly>
        <IForm v-model="metaDataSchema" :disabled="loading">
          <IRow>
            <IColumn xs="2">
              <IFormGroup required>
                <IFormLabel for="leaseOfferDate">Offer date</IFormLabel>
                <IInput id="leaseOfferDate" name="leaseOfferDate" type="date" :error="errorTypes" />
                <IFormError for="leaseOfferDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="5">
              <IFormGroup required>
                <IFormLabel for="landlord">Landlord</IFormLabel>
                <IInput id="landlord" name="landlord" :error="errorTypes" />
                <IFormError for="landlord" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="5">
              <IFormGroup required>
                <IFormLabel for="tenant">Tenant</IFormLabel>
                <IInput id="tenant" name="tenant" :error="errorTypes" />
                <IFormError for="tenant" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="6">
              <IFormGroup required>
                <IFormLabel for="leasedAddress">Leased address</IFormLabel>
                <IInput id="leasedAddress" name="leasedAddress" :error="errorTypes" />
                <IFormError for="leasedAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup required>
                <IFormLabel for="startDate">Start date</IFormLabel>
                <IInput id="startDate" name="startDate" type="month" :error="errorTypes" />
                <IFormError for="startDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup required>
                <IFormLabel for="endDate">End date</IFormLabel>
                <IInput id="endDate" name="endDate" type="month" :error="errorTypes" />
                <IFormError for="endDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="3">
              <IFormGroup required>
                <IFormLabel for="monthlyRentInDollars">Total rent ($)</IFormLabel>
                <IInput
                  id="monthlyRentInDollars"
                  name="monthlyRentInDollars"
                  type="number"
                  step="1"
                  :error="errorTypes"
                />
                <IFormError for="monthlyRentInDollars" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup required>
                <IFormLabel for="dueWithinDaysNumber">Due within days</IFormLabel>
                <IInput id="dueWithinDaysNumber" name="dueWithinDaysNumber" type="number" :error="errorTypes" />
                <IFormError for="dueWithinDaysNumber" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup required>
                <IFormLabel for="lateFeeInDollars">Late fee ($)</IFormLabel>
                <IInput id="lateFeeInDollars" name="lateFeeInDollars" type="number" :error="errorTypes" />
                <IFormError for="lateFeeInDollars" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup required>
                <IFormLabel for="securityDepositInDollars">Security deposit ($)</IFormLabel>
                <IInput
                  id="securityDepositInDollars"
                  name="securityDepositInDollars"
                  type="number"
                  :error="errorTypes"
                />
                <IFormError for="securityDepositInDollars" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="3">
              <IFormGroup>
                <IFormLabel for="proRatedRentInDollars">Pro-rated rent ($)</IFormLabel>
                <IInput
                  id="proRatedRentInDollars"
                  name="proRatedRentInDollars"
                  type="number"
                  step="1"
                  :error="errorTypes"
                />
                <IFormError for="proRatedRentInDollars" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup>
                <IFormLabel for="proRatedPaymentDate">Pro rated payment date</IFormLabel>
                <IInput id="proRatedPaymentDate" name="proRatedPaymentDate" type="date" :error="errorTypes" />
                <IFormError for="proRatedPaymentDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup>
                <IFormLabel for="proRatedLeaseStartDate">Pro rated lease start date</IFormLabel>
                <IInput id="proRatedLeaseStartDate" name="proRatedLeaseStartDate" type="date" :error="errorTypes" />
                <IFormError for="proRatedLeaseStartDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup>
                <IFormLabel for="proRatedLeaseEndDate">Pro rated lease end date</IFormLabel>
                <IInput id="proRatedLeaseEndDate" name="proRatedLeaseEndDate" type="date" :error="errorTypes" />
                <IFormError for="proRatedLeaseEndDate" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn xs="6">
              <IFormGroup required>
                <IFormLabel for="rentRecipient">Rent recipient</IFormLabel>
                <IInput id="rentRecipient" name="rentRecipient" :error="errorTypes" />
                <IFormError for="rentRecipient" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="6">
              <IFormGroup required>
                <IFormLabel for="rentRecipientAddress">Rent recipient address</IFormLabel>
                <IInput id="rentRecipientAddress" name="rentRecipientAddress" :error="errorTypes" />
                <IFormError for="rentRecipientAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="occupyingPersons">Occupying persons</IFormLabel>
                <IInput id="occupyingPersons" name="occupyingPersons" :error="errorTypes" />
                <IFormError for="occupyingPersons" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="numKeys">Number of key(s)</IFormLabel>
                <IInput id="numKeys" name="numKeys" type="number" :error="errorTypes" />
                <IFormError for="numKeys" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Termination notice</p>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="monthToMonthTerminationDaysPriorToEndOfLeaseTerm"
                  >Month-to-month written termination notice days prior to initial term end</IFormLabel
                >
                <IInput
                  id="monthToMonthTerminationDaysPriorToEndOfLeaseTerm"
                  name="monthToMonthTerminationDaysPriorToEndOfLeaseTerm"
                  type="number"
                  :error="errorTypes"
                />
                <IFormError for="monthToMonthTerminationDaysPriorToEndOfLeaseTerm" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="monthToMonthTerminationDaysPriorToFinalRentalMonth"
                  >Month-to-month written termination notice days prior to final rental month</IFormLabel
                >
                <IInput
                  id="monthToMonthTerminationDaysPriorToFinalRentalMonth"
                  name="monthToMonthTerminationDaysPriorToFinalRentalMonth"
                  type="number"
                  :error="errorTypes"
                />
                <IFormError for="monthToMonthTerminationDaysPriorToFinalRentalMonth" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="yearToYearNotice"
                  >Year-to-year written termination notice days prior to final rental month</IFormLabel
                >
                <IInput id="yearToYearNotice" name="yearToYearNotice" :error="errorTypes" />
                <IFormError for="yearToYearNotice" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Delivery of Notices</p>
          </IRow>
          <IRow>
            <IColumn xs="3">
              <IFormGroup required>
                <IFormLabel for="noticeName">Name</IFormLabel>
                <IInput id="noticeName" name="noticeName" :error="errorTypes" />
                <IFormError for="noticeName" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="6">
              <IFormGroup required>
                <IFormLabel for="noticeAddress">Address</IFormLabel>
                <IInput id="noticeAddress" name="noticeAddress" :error="errorTypes" />
                <IFormError for="noticeAddress" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn xs="3">
              <IFormGroup required>
                <IFormLabel for="noticePhone">Phone number</IFormLabel>
                <IInput id="noticePhone" name="noticePhone" :error="errorTypes" />
                <IFormError for="noticePhone" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="sendNoticeTo">Send notice to</IFormLabel>
                <IRadioGroup
                  id="sendNoticeTo"
                  name="sendNoticeTo"
                  :options="[
                    { id: 'yes', label: 'Landlord' },
                    { id: 'no', label: 'Property Manager' },
                  ]"
                />
                <IFormError for="sendNoticeTo" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Pets</p>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="authorizedToHavePets">Authorized to have pets?</IFormLabel>
                <IRadioGroup
                  id="authorizedToHavePets"
                  name="authorizedToHavePets"
                  :options="[
                    { id: 'yes', label: 'Yes' },
                    { id: 'no', label: 'No' },
                  ]"
                />
                <IFormError for="authorizedToHavePets" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="petsAllowedNumber">Number of pets</IFormLabel>
                <IInput id="petsAllowedNumber" name="petsAllowedNumber" type="number" />
                <IFormError for="petsAllowedNumber" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="petsAllowedType">Types of pets</IFormLabel>
                <IInput id="petsAllowedType" name="petsAllowedType" />
                <IFormError for="petsAllowedType" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="petsAllowedWeight">Weight of pets (lbs)</IFormLabel>
                <IInput id="petsAllowedWeight" name="petsAllowedWeight" />
                <IFormError for="petsAllowedWeight" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="petDepositInDollars">Pet deposit ($)</IFormLabel>
                <IInput id="petDepositInDollars" name="petDepositInDollars" type="number" />
                <IFormError for="petDepositInDollars" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="authorizedToHaveServiceAnimal">Authorized to have service animal?</IFormLabel>
                <IRadioGroup
                  id="authorizedToHaveServiceAnimal"
                  name="authorizedToHaveServiceAnimal"
                  :options="[
                    { id: 'yes', label: 'Yes' },
                    { id: 'no', label: 'No' },
                  ]"
                />
                <IFormError for="authorizedToHaveServiceAnimal" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="serviceAnimalsAllowedNumber">Number of service animals</IFormLabel>
                <IInput id="serviceAnimalsAllowedNumber" name="serviceAnimalsAllowedNumber" type="number" />
                <IFormError for="serviceAnimalsAllowedNumber" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="serviceAnimalsAllowedType">Types of service animals</IFormLabel>
                <IInput id="serviceAnimalsAllowedType" name="serviceAnimalsAllowedType" />
                <IFormError for="serviceAnimalsAllowedType" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn></IColumn>
            <IColumn></IColumn>
          </IRow>
          <IRow>
            <p class="lead">Inclusions / Exclusions</p>
          </IRow>
          <IRow class="inclusions">
            <IColumn>
              <ICheckbox name="incl_1" label="Alarm System" /><br />
              <ICheckbox name="incl_2" label="Carbon Monoxide Detector(s)" />#___<br />
              <ICheckbox name="incl_3" label="Ceiling Fan(s)" /> # ___<br />
              <ICheckbox name="incl_4" label="Central Vacuum" /><br />
              <ICheckbox name="incl_5" label="Clothes Dryer" /><br />
              <ICheckbox name="incl_6" label="Clothes Washer" /><br />
              <ICheckbox name="incl_7" label="Cooktop" /><br />
              <ICheckbox name="incl_8" label="Dishwasher" /><br />
              <ICheckbox name="incl_9" label="Drapery/Curtain Rods" /><br />
              <ICheckbox name="incl_10" label="Draperies/Curtains" /><br />
              <ICheckbox name="incl_11" label="Electronic Air Filter" /><br />
              <ICheckbox name="incl_12" label="Exhaust Fan(s)" /># ___<br />
              <ICheckbox name="incl_13" label="Exist. w/w Carpet" />
            </IColumn>
            <IColumn>
              <ICheckbox name="incl_14" label="Fireplace Screen Doors" /><br />
              <ICheckbox name="incl_15" label="Freezer" /><br />
              <ICheckbox name="incl_16" label="Furnace Humidifier" /><br />
              <ICheckbox name="incl_17" label="Garage Opener(s)" />#____<br />
              <ICheckbox name="incl_18" label="w/remote(s)" />#____<br />
              <ICheckbox name="incl_19" label="Garbage Disposer" /><br />
              <ICheckbox name="incl_20" label="Hot Tub, Equip. & Cover" /><br />
              <ICheckbox name="incl_21" label="Intercom" /><br />
              <ICheckbox name="incl_22" label="Lawn Mowers(s)" /><br />
              <ICheckbox name="incl_23" label="Microwave" /><br />
              <ICheckbox name="incl_24" label="Playground Equipment" /><br />
              <ICheckbox name="incl_25" label="Pool, Equip. & Cover" /><br />
              <ICheckbox name="incl_26" label="Refrigerator(s)" /># ____<br />
              <ICheckbox name="incl_27" label="w/ice maker" /><br />
            </IColumn>
            <IColumn>
              <ICheckbox name="incl_28" label="Satellite Dish" /><br />
              <ICheckbox name="incl_29" label="Screens" /><br />
              <ICheckbox name="incl_30" label="Shades/Blinds" /><br />
              <ICheckbox name="incl_31" label="Storage Shed(s)" /># ____<br />
              <ICheckbox name="incl_32" label="Storm Doors" /><br />
              <ICheckbox name="incl_33" label="Storm Windows" /><br />
              <ICheckbox name="incl_34" label="Stove or Range" /><br />
              <ICheckbox name="incl_35" label="T.V. Antenna" /><br />
              <ICheckbox name="incl_36" label="Trash Compactor" /><br />
              <ICheckbox name="incl_37" label="Wall Oven(s)" /> #____<br />
              <ICheckbox name="incl_38" label="Water Filter" /><br />
              <ICheckbox name="incl_39" label="Water Softener" /><br />
              <ICheckbox name="incl_40" label="Window A/C Unit(s)" />#____<br />
            </IColumn>
            <IColumn>
              <ICheckbox name="incl_41" label="Window Fan(s)" />#____<br />
              <ICheckbox name="incl_42" label="Wood Stove" /><br />
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="additionalInclusions">Additonal inclusions</IFormLabel>
                <IInput id="additionalInclusions" name="additionalInclusions" :error="errorTypes" />
                <IFormError for="additionalInclusions" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="additionalExclusions">Additonal exclusions</IFormLabel>
                <IInput id="additionalExclusions" name="additionalExclusions" :error="errorTypes" />
                <IFormError for="additionalExclusions" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Utilities</p>
          </IRow>
          <IRow>
            <IColumn>
              <table class="utilities">
                <tr>
                  <td>a. Cable TV</td>
                  <td>
                    <ICheckbox name="furnishedByLandlord_1" label="Landlord" />
                  </td>
                  <td>
                    <ICheckbox name="furnishedByTenant_1" label="Tenant" />
                  </td>
                </tr>
                <tr>
                  <td>b. Cold Water/Sewer</td>
                  <td>
                    <ICheckbox name="furnishedByLandlord_2" label="Landlord" />
                  </td>
                  <td>
                    <ICheckbox name="furnishedByTenant_2" label="Tenant" />
                  </td>
                </tr>
                <tr>
                  <td>c. Cooking Fuel</td>
                  <td>
                    <ICheckbox name="furnishedByLandlord_3" label="Landlord" />
                  </td>
                  <td>
                    <ICheckbox name="furnishedByTenant_3" label="Tenant" />
                  </td>
                </tr>
                <tr>
                  <td>d. Electricity</td>
                  <td>
                    <ICheckbox name="furnishedByLandlord_4" label="Landlord" />
                  </td>
                  <td>
                    <ICheckbox name="furnishedByTenant_4" label="Tenant" />
                  </td>
                </tr>
                <tr>
                  <td>e. Heating Fuel</td>
                  <td>
                    <ICheckbox name="furnishedByLandlord_5" label="Landlord" />
                  </td>
                  <td>
                    <ICheckbox name="furnishedByTenant_5" label="Tenant" />
                  </td>
                </tr>
                <tr>
                  <td>f. Heating of Water</td>
                  <td>
                    <ICheckbox name="furnishedByLandlord_6" label="Landlord" />
                  </td>
                  <td>
                    <ICheckbox name="furnishedByTenant_6" label="Tenant" />
                  </td>
                </tr>
                <tr>
                  <td>g. Trash Removal</td>
                  <td>
                    <ICheckbox name="furnishedByLandlord_7" label="Landlord" />
                  </td>
                  <td>
                    <ICheckbox name="furnishedByTenant_7" label="Tenant" />
                  </td>
                </tr>
              </table>
            </IColumn>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="oilPropane">In the event the Property uses oil/propane for heat:</IFormLabel>
                <ITextarea id="oilPropane" name="oilPropane" :error="errorTypes" />
                <IFormError for="oilPropane" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Maintenance</p>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="additionalMaintenance">Additonal maintenance</IFormLabel>
                <ITextarea id="additionalMaintenance" name="additionalMaintenance" :error="errorTypes" />
                <IFormError for="additionalMaintenance" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup>
                <IFormLabel for="poolCompany">Pool company</IFormLabel>
                <ITextarea id="poolCompany" name="poolCompany" :error="errorTypes" />
                <IFormError for="poolCompany" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Showing of property</p>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="showingDays">Days prior to end of lease</IFormLabel>
                <IInput id="showingDays" name="showingDays" type="number" :error="errorTypes" />
                <IFormError for="showingDays" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="showingStartTime">Showing start time</IFormLabel>
                <IInput id="showingStartTime" name="showingStartTime" type="time" :error="errorTypes" />
                <IFormError for="showingStartTime" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="showingEndTime">Showing end time</IFormLabel>
                <IInput id="showingEndTime" name="showingEndTime" type="time" :error="errorTypes" />
                <IFormError for="showingEndTime" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Age classification of property</p>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="federalAgeClassification">The Federal Program (check one)</IFormLabel>
                <IRadioGroup
                  id="federalAgeClassification"
                  name="federalAgeClassification"
                  :options="[
                    {
                      id: 'no',
                      label: 'the Property was built during or after 1978; the Federal Program does not apply',
                    },
                    { id: 'yes', label: 'the Property was built before 1978; the Federal Program applies' },
                  ]"
                />
                <IFormError for="federalAgeClassification" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup required>
                <IFormLabel for="stateAgeClassification">The Federal Program (check one)</IFormLabel>
                <IRadioGroup
                  id="stateAgeClassification"
                  name="stateAgeClassification"
                  :options="[
                    { id: 'yes', label: 'the Property was built prior to 1978; the Maryland Program applies.' },
                    {
                      id: 'no',
                      label: 'the Property was built during or after 1978; the Maryland Program does not apply.',
                    },
                  ]"
                />
                <IFormError for="stateAgeClassification" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Additional provisions</p>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup>
                <ITextarea id="additionalProvisions" name="additionalProvisions" :error="errorTypes" />
                <IFormError for="additionalProvisions" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Addendum(s) attched concerning</p>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup>
                <ITextarea name="addendumAttachedConcerning" :error="errorTypes" />
                <IFormError for="addendumAttachedConcerning" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
          <IRow>
            <p class="lead">Addendum</p>
          </IRow>
          <IRow>
            <IColumn>
              <IFormGroup>
                <ITextarea name="addendum" :error="errorTypes" />
                <IFormError for="addendum" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
        </IForm>
        <IRow>
          <p class="lead">Recipients</p>
        </IRow>
        <IForm v-model="recipientsSchema" :disabled="loading">
          <IRow v-for="(_, index) in recipientsSchema.recipients" :key="index">
            <IColumn xs="6">
              <IFormGroup required>
                <IFormLabel :for="`recipients.${index}.name`">
                  <span v-if="index === 0">Landlord name</span>
                  <span v-else>Tenant name {{ index }}</span>
                </IFormLabel>
                <IInput :name="`recipients.${index}.name`" :error="errorTypes" />
                <IFormError :for="`recipients.${index}`" :visible="errorTypes" />
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
                <IFormError :for="`recipients.${index}`" :visible="errorTypes" />
              </IFormGroup>
            </IColumn>
          </IRow>
        </IForm>
        <IRow>
          <IColumn>
            <div class="save">
              <div class="_display:flex _justify-content:space-between">
                <div class="left-buttons">
                  <IButton @click="loadTestData()">Load Test Data</IButton>
                  <IButton @click="addRecipient">Add recipient</IButton>
                  <IButton @click="open = true">Preview</IButton>
                </div>
                <div v-if="!loading" class="right-buttons">
                  <DeleteAgreementButton :id="agreementId" :isNew="isNew" />
                  <IButton v-if="!sent" color="primary" :loading="saving" @click="save">Save</IButton>
                </div>
              </div>
            </div>
          </IColumn>
        </IRow>
      </ClientOnly>
    </IContainer>
  </ILayout>
</template>
<script setup>
import { provide } from 'vue'
import { inject } from 'vue'
import { useForm } from "@inkline/inkline/composables";
const dayjs = useDayjs();
const {
  params: { id: agreementId },
} = useRoute();

const pdfType = "LEASE_AGREEMENT_MD_V1";
const isNew = agreementId === "New";

const fieldOptions = {
  validators: [
    {
      name: "required",
    },
  ],
};

const emailFieldOptions = {
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
};

const sent = ref();
const open = ref(false);

provide('sideBarOpen', open);

const {
  schema: metaDataSchema,
  form: metadataForm,
  validate,
} = useForm({
  leaseOfferDate: { ...fieldOptions, value: dayjs().format("YYYY-MM-DD") },
  landlord: fieldOptions,
  tenant: fieldOptions,
  leasedAddress: fieldOptions,
  startDate: fieldOptions,
  endDate: fieldOptions,
  monthlyRentInDollars: fieldOptions,
  proRatedRentInDollars: {},
  proRatedPaymentDate: {},
  proRatedLeaseStartDate: {},
  proRatedLeaseEndDate: {},
  rentRecipient: fieldOptions,
  rentRecipientAddress: fieldOptions,
  occupyingPersons: fieldOptions,
  monthToMonthTerminationDaysPriorToEndOfLeaseTerm: { ...fieldOptions, value: 30 },
  monthToMonthTerminationDaysPriorToFinalRentalMonth: { ...fieldOptions, value: 60 },
  dueWithinDaysNumber: { ...fieldOptions, value: 5 },
  lateFeeInDollars: { ...fieldOptions, value: 50 },
  yearToYearNotice: {},
  noticeName: fieldOptions,
  noticeAddress: fieldOptions,
  noticePhone: fieldOptions,
  sendNoticeTo: { ...fieldOptions, value: "yes" },
  securityDepositInDollars: fieldOptions,
  authorizedToHavePets: fieldOptions,
  authorizedToHaveServiceAnimal: fieldOptions,
  petsAllowedNumber: {},
  serviceAnimalsAllowedNumber: {},
  petsAllowedType: {},
  serviceAnimalsAllowedType: {},
  petsAllowedWeight: {},
  petDepositInDollars: {},
  numKeys: fieldOptions,
  incl_1: { value: false },
  incl_1: { value: false },
  incl_2: { value: false },
  incl_3: { value: false },
  incl_4: { value: false },
  incl_5: { value: true },
  incl_6: { value: true },
  incl_7: { value: false },
  incl_8: { value: true },
  incl_9: { value: false },
  incl_10: { value: false },
  incl_11: { value: false },
  incl_12: { value: false },
  incl_13: { value: false },
  incl_14: { value: false },
  incl_15: { value: false },
  incl_16: { value: false },
  incl_17: { value: false },
  incl_18: { value: false },
  incl_19: { value: false },
  incl_20: { value: false },
  incl_21: { value: false },
  incl_22: { value: false },
  incl_23: { value: true },
  incl_24: { value: false },
  incl_25: { value: false },
  incl_26: { value: true },
  incl_27: { value: true },
  incl_28: { value: false },
  incl_29: { value: true },
  incl_30: { value: true },
  incl_31: { value: false },
  incl_32: { value: false },
  incl_33: { value: false },
  incl_34: { value: true },
  incl_35: { value: false },
  incl_36: { value: false },
  incl_37: { value: false },
  incl_38: { value: false },
  incl_39: { value: false },
  incl_40: { value: false },
  incl_41: { value: false },
  incl_42: { value: false },
  additionalInclusions: { value: "N/A" },
  additionalExclusions: { value: "N/A" },
  furnishedByLandlord_1: { value: false },
  furnishedByTenant_1: { value: true },
  furnishedByLandlord_2: { value: false },
  furnishedByTenant_2: { value: true },
  furnishedByLandlord_3: { value: false },
  furnishedByTenant_3: { value: true },
  furnishedByLandlord_4: { value: false },
  furnishedByTenant_4: { value: true },
  furnishedByLandlord_5: { value: false },
  furnishedByTenant_5: { value: true },
  furnishedByLandlord_6: { value: false },
  furnishedByTenant_6: { value: true },
  furnishedByLandlord_7: { value: true },
  furnishedByTenant_7: { value: false },
  oilPropane: { value: "N/A" },
  additionalMaintenance: {
    value:
      'REPAIR DEDUCTIBLE: Tenant is responsible for first $200.00 of every repair; Other provision listed on "Addendum to Contract". Renters insurance.',
  },
  showingDays: { ...fieldOptions, value: 30 },
  showingStartTime: { ...fieldOptions, value: "09:00" },
  showingEndTime: { ...fieldOptions, value: "20:00" },
  poolCompany: { value: "N/A" },
  federalAgeClassification: fieldOptions,
  stateAgeClassification: fieldOptions,
  ageUnknown: { ...fieldOptions, value: false },
  additionalProvisions: { value: "Utilities are to be kept under tenant's name for the duration of the lease" },
  addendumAttachedConcerning: { value: "Renters Insurance, Maintenance, & Repairs" },
  addendum: {},
});

const {
  schema: recipientsSchema,
  form: recipientsForm,
  validate: recipientsValidate,
} = useForm({
  recipients: [
    {
      name: fieldOptions,
      email: emailFieldOptions,
    },
    {
      name: fieldOptions,
      email: emailFieldOptions,
    },
  ],
});

const errorTypes = ["touched", "invalid"];
const saving = ref(false);
const loading = ref(false);

const loadTestData = async () => {
  metaDataSchema.value.landlord.value = "Evangelos Poneres";
  metaDataSchema.value.tenant.value = "Stephan Michael Nutty, Kristy Diane Nutty";
  metaDataSchema.value.leasedAddress.value = "10722 LANCEWOOD RD, COCKEYSVILLE, MD 21030";
  metaDataSchema.value.startDate.value = dayjs().format("YYYY-MM");
  metaDataSchema.value.endDate.value = dayjs().add(1, "year").add(-1, "month").format("YYYY-MM");
  metaDataSchema.value.monthlyRentInDollars.value = 2000;
  metaDataSchema.value.proRatedRentInDollars.value = 1000;
  metaDataSchema.value.proRatedPaymentDate.value = dayjs().add(-8, "day").format("YYYY-MM-DD");
  metaDataSchema.value.proRatedLeaseStartDate.value = dayjs().add(-5, "day").format("YYYY-MM-DD");
  metaDataSchema.value.proRatedLeaseEndDate.value = dayjs().add(-1, "day").format("YYYY-MM-DD");
  metaDataSchema.value.rentRecipient.value = "Evangelos Poneres";
  metaDataSchema.value.rentRecipientAddress.value = "PO Box 11, Park Ridge, IL 60068";
  metaDataSchema.value.occupyingPersons.value = "Snoopy";
  metaDataSchema.value.numKeys.value = 2;

  metaDataSchema.value.yearToYearNotice.value = "N/A";
  metaDataSchema.value.noticeName.value = "Evangelos Poneres";
  metaDataSchema.value.noticeAddress.value = "PO Box 11, Park Ridge, IL 60068";
  metaDataSchema.value.noticePhone.value = "444 555 1234";
  metaDataSchema.value.securityDepositInDollars.value = 3000;

  metaDataSchema.value.authorizedToHavePets.value = "yes";
  metaDataSchema.value.authorizedToHaveServiceAnimal.value = "no";
  metaDataSchema.value.petsAllowedNumber.value = 1;
  metaDataSchema.value.serviceAnimalsAllowedNumber.value = 2;
  metaDataSchema.value.petsAllowedType.value = "Mutt";
  metaDataSchema.value.serviceAnimalsAllowedType.value = "Hound";
  metaDataSchema.value.petsAllowedWeight.value = "125 lbs";
  metaDataSchema.value.petDepositInDollars.value = 450;

  metaDataSchema.value.furnishedByTenant_1.value = true;
  metaDataSchema.value.furnishedByTenant_2.value = true;
  metaDataSchema.value.furnishedByTenant_3.value = true;
  metaDataSchema.value.furnishedByTenant_4.value = true;
  metaDataSchema.value.furnishedByTenant_5.value = true;
  metaDataSchema.value.furnishedByTenant_6.value = true;
  metaDataSchema.value.furnishedByLandlord_7.value = true;
  metaDataSchema.value.oilPropane.value = "N/A";
  metaDataSchema.value.additionalMaintenance.value = "N/A";
  metaDataSchema.value.addendum.value = "Sample";
  metaDataSchema.value.federalAgeClassification.value = "no";
  metaDataSchema.value.stateAgeClassification.value = "no";

  metaDataSchema.value.touched = true;

  recipientsSchema.value.recipients[0].name.value = "John Wick";
  recipientsSchema.value.recipients[0].email.value = "landlord@poneres.com";
  recipientsSchema.value.recipients[1].name.value = "Stephan Michael Nutty";
  recipientsSchema.value.recipients[1].email.value = "tenant_a@hello.io";
  recipientsSchema.value.touched = true;
};

onMounted(async () => {
  if (isNew) {
    return;
  }
  loading.value = true;

  const agreement = await getAgreement(agreementId);
  sent.value = agreement.sent;
  Object.keys(agreement.metadata).forEach((k) => {
    metaDataSchema.value[k].value = agreement.metadata[k];
  });

  agreement.recipients.forEach((recipient, index) => {
    if (index > 1) {
      addRecipient();
    }
    recipientsSchema.value.recipients[index].email.value = recipient.email;
    recipientsSchema.value.recipients[index].name.value = recipient.name;
  });

  loading.value = false;
});

const save = async () => {
  await validate();
  await recipientsValidate();

  if (!metaDataSchema.value.valid || !recipientsSchema.value.valid) {
    return;
  }

  saving.value = true;

  await saveAgreement({
    id: isNew ? undefined : agreementId,
    type: pdfType,
    metadata: metadataForm.value,
    recipients: recipientsForm.value.recipients,
  }).then(() => {
    navigateTo("/agreements");
  });
};

const addRecipient = () => {
  recipientsSchema.value.recipients.push({
    name: { ...fieldOptions },
    email: { ...emailFieldOptions },
  });
};

const removeRecipient = (index) => {
  if (recipientsSchema.value.recipients.length === 1) {
    return;
  }
  recipientsSchema.value.recipients.splice(index, 1);
};
</script>
<style scoped>
.save {
  margin-top: 18px;
  text-align: center;
}

.left-buttons button {
  margin: 6px;
}

.right-buttons button {
  margin: 6px;
}

.sidebar {
  --sidebar--width: 1100px;
}

input {
  text-transform: uppercase !important;
}

.remove-recipient {
  margin-top: 30px;
}

.inclusions .checkbox {
  display: inline;
}

.utilities .checkbox {
  display: inline;
  margin: 12px;
}
</style>
