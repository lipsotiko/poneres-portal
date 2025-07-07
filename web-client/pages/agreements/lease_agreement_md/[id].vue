<template>
  <Agreement
    pdf-type="LEASE_AGREEMENT_MD_V1"
    title="Residential Dwelling Lease (Maryland)"
    :agreement-id="agreementId"
    :schema="metaDataSchema"
    :test-data="testData"
  >
    <div class="grid grid-cols-12 gap-4 py-2">
      <div class="col-span-2">
        <FormField v-slot="{ componentField }" name="leaseOfferDate">
          <FormItem>
            <FormLabel>Offer date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="date" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-5">
        <FormField v-slot="{ componentField }" name="landlord">
          <FormItem>
            <FormLabel>Landlord</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-5">
        <FormField v-slot="{ componentField }" name="tenant">
          <FormItem>
            <FormLabel>Tenant</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-12 gap-4 py-2">
      <div class="col-span-6">
        <FormField v-slot="{ componentField }" name="leasedAddress">
          <FormItem>
            <FormLabel>Leased address</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="startDate">
          <FormItem>
            <FormLabel>Start date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="month" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="endDate">
          <FormItem>
            <FormLabel>End date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="month" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-12 gap-4 py-2">
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="monthlyRentInDollars">
          <FormItem>
            <FormLabel>Total rent ($)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" step="1" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="dueWithinDaysNumber">
          <FormItem>
            <FormLabel>Due within days</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="lateFeeInDollars">
          <FormItem>
            <FormLabel>Late fee ($)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="securityDepositInDollars">
          <FormItem>
            <FormLabel>Security deposit ($)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-12 gap-4 py-2">
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="proRatedRentInDollars">
          <FormItem>
            <FormLabel>Pro-rated rent ($)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" step="1" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="proRatedPaymentDate">
          <FormItem>
            <FormLabel>Pro rated payment date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="date" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="proRatedLeaseStartDate">
          <FormItem>
            <FormLabel>Pro rated lease start date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="date" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="proRatedLeaseEndDate">
          <FormItem>
            <FormLabel>Pro rated lease end date</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="date" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-2 gap-4 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="rentRecipient">
          <FormItem>
            <FormLabel>Rent recipient</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="rentRecipientAddress">
          <FormItem>
            <FormLabel>Rent recipient address</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-2 gap-4 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="occupyingPersons">
          <FormItem>
            <FormLabel>Occupying persons</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="numKeys">
          <FormItem>
            <FormLabel>Number of key(s)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Termination notice</p>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="monthToMonthTerminationDaysPriorToEndOfLeaseTerm">
          <FormItem>
            <FormLabel>Month-to-month written termination notice days prior to initial term end</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="monthToMonthTerminationDaysPriorToFinalRentalMonth">
          <FormItem>
            <FormLabel>Month-to-month written termination notice days prior to final rental month </FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="yearToYearNotice">
          <FormItem>
            <FormLabel>Year-to-year written termination notice days prior to final rental month</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Delivery of Notices</p>
    </div>
    <div class="grid grid-cols-12 gap-4 py-2">
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="noticeName">
          <FormItem>
            <FormLabel>Name</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-6">
        <FormField v-slot="{ componentField }" name="noticeAddress">
          <FormItem>
            <FormLabel>Address</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div class="col-span-3">
        <FormField v-slot="{ componentField }" name="noticePhone">
          <FormItem>
            <FormLabel>Phone</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="sendNoticeTo">
          <FormItem>
            <FormLabel>Send notice to</FormLabel>
            <FormControl>
              <RadioGroup v-bind="componentField">
                <div
                  v-for="o in [
                    { id: 'yes', label: 'Landlord' },
                    { id: 'no', label: 'Property Manager' },
                  ]"
                  class="flex items-center space-x-2"
                >
                  <RadioGroupItem :id="o.id" :value="o.id" />
                  <Label :for="o.id">{{ o.label }}</Label>
                </div>
              </RadioGroup>
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Pets</p>
    </div>
    <div class="grid grid-cols-5 gap-4 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="authorizedToHavePets">
          <FormItem>
            <FormLabel>Authorized to have pets?</FormLabel>
            <FormControl>
              <RadioGroup v-bind="componentField">
                <div
                  v-for="o in [
                    { id: 'yes', label: 'Yes' },
                    { id: 'no', label: 'No' },
                  ]"
                  class="flex items-center space-x-2"
                >
                  <RadioGroupItem :id="o.id" :value="o.id" />
                  <Label :for="o.id">{{ o.label }}</Label>
                </div>
              </RadioGroup>
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="petsAllowedNumber">
          <FormItem>
            <FormLabel>Number of pets</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="petsAllowedType">
          <FormItem>
            <FormLabel>Types of pets</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="petsAllowedWeight">
          <FormItem>
            <FormLabel>Weight of pets (lbs)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="petDepositInDollars">
          <FormItem>
            <FormLabel>Pet deposit ($)</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="grid grid-cols-5 gap-4 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="authorizedToHaveServiceAnimal">
          <FormItem>
            <FormLabel>Authorized to have service animal?</FormLabel>
            <FormControl>
              <RadioGroup v-bind="componentField">
                <div
                  v-for="o in [
                    { id: 'yes', label: 'Yes' },
                    { id: 'no', label: 'No' },
                  ]"
                  class="flex items-center space-x-2"
                >
                  <RadioGroupItem :id="o.id" :value="o.id" />
                  <Label :for="o.id">{{ o.label }}</Label>
                </div>
              </RadioGroup>
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="serviceAnimalsAllowedNumber">
          <FormItem>
            <FormLabel>Number of service animals</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="serviceAnimalsAllowedType">
          <FormItem>
            <FormLabel>Types of service animals</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div></div>
      <div></div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Inclusions / Exclusions</p>
    </div>
    <div class="grid grid-cols-4 gap-4 py-2 inclusions">
      <div>
        <Checkbox2 name="incl_1" label="Alarm System" />
        <Checkbox2 name="incl_2" label="Carbon Monoxide Detector(s)" />
        <Checkbox2 name="incl_3" label="Ceiling Fan(s)" />
        <Checkbox2 name="incl_4" label="Central Vacuum" />
        <Checkbox2 name="incl_5" label="Clothes Dryer" />
        <Checkbox2 name="incl_6" label="Clothes Washer" />
        <Checkbox2 name="incl_7" label="Cooktop" />
        <Checkbox2 name="incl_8" label="Dishwasher" />
        <Checkbox2 name="incl_9" label="Drapery/Curtain Rods" />
        <Checkbox2 name="incl_10" label="Draperies/Curtains" />
        <Checkbox2 name="incl_11" label="Electronic Air Filter" />
        <Checkbox2 name="incl_12" label="Exhaust Fan(s)" />
        <Checkbox2 name="incl_13" label="Exist. w/w Carpet" />
      </div>
      <div>
        <Checkbox2 name="incl_14" label="Fireplace Screen Doors" />
        <Checkbox2 name="incl_15" label="Freezer" />
        <Checkbox2 name="incl_16" label="Furnace Humidifier" />
        <Checkbox2 name="incl_17" label="Garage Opener(s)" />
        <Checkbox2 name="incl_18" label="w/remote(s)" />
        <Checkbox2 name="incl_19" label="Garbage Disposer" />
        <Checkbox2 name="incl_20" label="Hot Tub, Equip. & Cover" />
        <Checkbox2 name="incl_21" label="Intercom" />
        <Checkbox2 name="incl_22" label="Lawn Mowers(s)" />
        <Checkbox2 name="incl_23" label="Microwave" />
        <Checkbox2 name="incl_24" label="Playground Equipment" />
        <Checkbox2 name="incl_25" label="Pool, Equip. & Cover" />
        <Checkbox2 name="incl_26" label="Refrigerator(s)" />
        <Checkbox2 name="incl_27" label="w/ice maker" />
      </div>
      <div>
        <Checkbox2 name="incl_28" label="Satellite Dish" />
        <Checkbox2 name="incl_29" label="Screens" />
        <Checkbox2 name="incl_30" label="Shades/Blinds" />
        <Checkbox2 name="incl_31" label="Storage Shed(s)" />
        <Checkbox2 name="incl_32" label="Storm Doors" />
        <Checkbox2 name="incl_33" label="Storm Windows" />
        <Checkbox2 name="incl_34" label="Stove or Range" />
        <Checkbox2 name="incl_35" label="T.V. Antenna" />
        <Checkbox2 name="incl_36" label="Trash Compactor" />
        <Checkbox2 name="incl_37" label="Wall Oven(s)" />
        <Checkbox2 name="incl_38" label="Water Filter" />
        <Checkbox2 name="incl_39" label="Water Softener" />
        <Checkbox2 name="incl_40" label="Window A/C Unit(s)" />
      </div>
      <div>
        <Checkbox2 name="incl_41" label="Window Fan(s)" />
        <Checkbox2 name="incl_42" label="Wood Stove" />
      </div>
    </div>
    <div class="py-2">
      <div>
        <IFormGroup>
          <IFormLabel for="additionalInclusions">Additonal inclusions</IFormLabel>
          <Input id="additionalInclusions" name="additionalInclusions" />
        </IFormGroup>
      </div>
    </div>
    <div class="py-2">
      <div>
        <IFormGroup>
          <IFormLabel for="additionalExclusions">Additonal exclusions</IFormLabel>
          <Input id="additionalExclusions" name="additionalExclusions" />
        </IFormGroup>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Utilities</p>
    </div>
    <div class="py-2">
      <div>
        <table class="utilities">
          <tbody>
            <tr>
              <td>a. Cable TV</td>
              <td>
                <Checkbox2 name="furnishedByLandlord_1" label="Landlord" />
              </td>
              <td>
                <Checkbox2 name="furnishedByTenant_1" label="Tenant" />
              </td>
            </tr>
            <tr>
              <td>b. Cold Water/Sewer</td>
              <td>
                <Checkbox2 name="furnishedByLandlord_2" label="Landlord" />
              </td>
              <td>
                <Checkbox2 name="furnishedByTenant_2" label="Tenant" />
              </td>
            </tr>
            <tr>
              <td>c. Cooking Fuel</td>
              <td>
                <Checkbox2 name="furnishedByLandlord_3" label="Landlord" />
              </td>
              <td>
                <Checkbox2 name="furnishedByTenant_3" label="Tenant" />
              </td>
            </tr>
            <tr>
              <td>d. Electricity</td>
              <td>
                <Checkbox2 name="furnishedByLandlord_4" label="Landlord" />
              </td>
              <td>
                <Checkbox2 name="furnishedByTenant_4" label="Tenant" />
              </td>
            </tr>
            <tr>
              <td>e. Heating Fuel</td>
              <td>
                <Checkbox2 name="furnishedByLandlord_5" label="Landlord" />
              </td>
              <td>
                <Checkbox2 name="furnishedByTenant_5" label="Tenant" />
              </td>
            </tr>
            <tr>
              <td>f. Heating of Water</td>
              <td>
                <Checkbox2 name="furnishedByLandlord_6" label="Landlord" />
              </td>
              <td>
                <Checkbox2 name="furnishedByTenant_6" label="Tenant" />
              </td>
            </tr>
            <tr>
              <td>g. Trash Removal</td>
              <td>
                <Checkbox2 name="furnishedByLandlord_7" label="Landlord" />
              </td>
              <td>
                <Checkbox2 name="furnishedByTenant_7" label="Tenant" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="py-2">
        <IFormGroup>
          <IFormLabel for="oilPropane">In the event the Property uses oil/propane for heat:</IFormLabel>
          <Textarea id="oilPropane" name="oilPropane" />
        </IFormGroup>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Maintenance</p>
    </div>
    <div class="grid grid-cols-2 gap-4 py-2 inclusions">
      <div>
        <IFormGroup>
          <IFormLabel for="additionalMaintenance">Additonal maintenance</IFormLabel>
          <Textarea id="additionalMaintenance" name="additionalMaintenance" />
        </IFormGroup>
      </div>
      <div>
        <IFormGroup>
          <IFormLabel for="poolCompany">Pool company</IFormLabel>
          <Textarea id="poolCompany" name="poolCompany" />
        </IFormGroup>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Showing of property</p>
    </div>
    <div class="grid grid-cols-3 gap-3 py-2">
      <div>
        <IFormGroup required>
          <IFormLabel for="showingDays">Days prior to end of lease</IFormLabel>
          <Input id="showingDays" name="showingDays" type="number" />
        </IFormGroup>
      </div>
      <div>
        <IFormGroup required>
          <IFormLabel for="showingStartTime">Showing start time</IFormLabel>
          <Input id="showingStartTime" name="showingStartTime" type="time" />
        </IFormGroup>
      </div>
      <div>
        <IFormGroup required>
          <IFormLabel for="showingEndTime">Showing end time</IFormLabel>
          <Input id="showingEndTime" name="showingEndTime" type="time" />
        </IFormGroup>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Age classification of property</p>
    </div>
    <div class="py-2">
      <div>
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
        </IFormGroup>
      </div>
    </div>
    <div class="py-2">
      <div>
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
        </IFormGroup>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Additional provisions</p>
    </div>
    <div class="py-2">
      <div>
        <IFormGroup>
          <Textarea id="additionalProvisions" name="additionalProvisions" />
        </IFormGroup>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Addendum(s) attched concerning</p>
    </div>
    <div class="py-2">
      <div>
        <IFormGroup>
          <Textarea id="addendumAttachedConcerning" name="addendumAttachedConcerning" />
        </IFormGroup>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Addendum</p>
    </div>
    <div class="py-2">
      <div>
        <IFormGroup>
          <Textarea id="addendum" name="addendum" />
        </IFormGroup>
      </div>
    </div>
    <Recipients
      recipientName-1="Landlord name"
      recipientEmail-1="Landlord email"
      recipientName-2="Tenant name"
      recipientEmail-2="Tenant email"
    />
  </Agreement>
</template>
<script setup>
import { z } from "zod/v4";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";

const {
  params: { id: agreementId },
} = useRoute();
const dayjs = useDayjs();

const metaDataSchema = {
  leaseOfferDate: z.string(),
  landlord: z.string(),
  tenant: z.string(),
  leasedAddress: z.string(),
  startDate: z.string(),
  endDate: z.string(),
  monthlyRentInDollars: z.number(),
  dueWithinDaysNumber: z.number(),
  lateFeeInDollars: z.number(),
  securityDepositInDollars: z.number(),
  proRatedRentInDollars: z.number(),
  proRatedPaymentDate: z.string(),
  proRatedLeaseStartDate: z.string(),
  proRatedLeaseEndDate: z.string(),
  rentRecipient: z.string(),
  rentRecipientAddress: z.string(),
  occupyingPersons: z.string(),
  numKeys: z.string(),
  monthToMonthTerminationDaysPriorToEndOfLeaseTerm: z.number(),
  monthToMonthTerminationDaysPriorToFinalRentalMonth: z.number(),
  yearToYearNotice: z.number().optional(),
  noticeName: z.string(),
  noticeAddress: z.string(),
  noticePhone: z.string(),
  sendNoticeTo: z.string(),
  authorizedToHavePets: z.string(),
  petsAllowedNumber: z.number().optional(),
  petsAllowedType: z.number().optional(),
  petsAllowedWeight: z.number().optional(),
  petDepositInDollars: z.number().optional(),
  authorizedToHaveServiceAnimal: z.number().optional(),
  serviceAnimalsAllowedNumber: z.number().optional(),
  serviceAnimalsAllowedType: z.string().optional(),
  // incl_1: { value: false },
  // incl_2: { value: false },
  // incl_3: { value: false },
  // incl_4: { value: false },
  // incl_5: { value: true },
  // incl_6: { value: true },
  // incl_7: { value: false },
  // incl_8: { value: true },
  // incl_9: { value: false },
  // incl_10: { value: false },
  // incl_11: { value: false },
  // incl_12: { value: false },
  // incl_13: { value: false },
  // incl_14: { value: false },
  // incl_15: { value: false },
  // incl_16: { value: false },
  // incl_17: { value: false },
  // incl_18: { value: false },
  // incl_19: { value: false },
  // incl_20: { value: false },
  // incl_21: { value: false },
  // incl_22: { value: false },
  // incl_23: { value: true },
  // incl_24: { value: false },
  // incl_25: { value: false },
  // incl_26: { value: true },
  // incl_27: { value: true },
  // incl_28: { value: false },
  // incl_29: { value: true },
  // incl_30: { value: true },
  // incl_31: { value: false },
  // incl_32: { value: false },
  // incl_33: { value: false },
  // incl_34: { value: true },
  // incl_35: { value: false },
  // incl_36: { value: false },
  // incl_37: { value: false },
  // incl_38: { value: false },
  // incl_39: { value: false },
  // incl_40: { value: false },
  // incl_41: { value: false },
  // incl_42: { value: false },
  // additionalInclusions: { value: "N/A" },
  // additionalExclusions: { value: "N/A" },
  // furnishedByLandlord_1: { value: false },
  // furnishedByTenant_1: { value: true },
  // furnishedByLandlord_2: { value: false },
  // furnishedByTenant_2: { value: true },
  // furnishedByLandlord_3: { value: false },
  // furnishedByTenant_3: { value: true },
  // furnishedByLandlord_4: { value: false },
  // furnishedByTenant_4: { value: true },
  // furnishedByLandlord_5: { value: false },
  // furnishedByTenant_5: { value: true },
  // furnishedByLandlord_6: { value: false },
  // furnishedByTenant_6: { value: true },
  // furnishedByLandlord_7: { value: true },
  // furnishedByTenant_7: { value: false },
  // oilPropane: { value: "N/A" },
  // additionalMaintenance: {
  //   value:
  //     'REPAIR DEDUCTIBLE: Tenant is responsible for first $200.00 of every repair; Other provision listed on "Addendum to Contract". Renters insurance.',
  // },
  // showingDays: { ...fieldOptions, value: 30 },
  // showingStartTime: { ...fieldOptions, value: "09:00" },
  // showingEndTime: { ...fieldOptions, value: "20:00" },
  // poolCompany: { value: "N/A" },
  // federalAgeClassification: fieldOptions,
  // stateAgeClassification: fieldOptions,
  // ageUnknown: { ...fieldOptions, value: false },
  // additionalProvisions: { value: "Utilities are to be kept under tenant's name for the duration of the lease" },
  // addendumAttachedConcerning: { value: "Renters Insurance, Maintenance, & Repairs" },
  // addendum: {},
};

const testData = {
  leaseOfferDate: dayjs().format("YYYY-MM-DD"),
  landlord: "Evangelos Poneres",
  tenant: "Stephan Michael Nutty, Kristy Diane Nutty",
  leasedAddress: "10722 LANCEWOOD RD, COCKEYSVILLE, MD 21030",
  startDate: dayjs().format("YYYY-MM"),
  endDate: dayjs().add(1, "year").add(-1, "month").format("YYYY-MM"),
  monthlyRentInDollars: 2000,
  dueWithinDaysNumber: 5,
  lateFeeInDollars: 50,
  proRatedRentInDollars: 1000,
  proRatedPaymentDate: dayjs().add(-8, "day").format("YYYY-MM-DD"),
  proRatedLeaseStartDate: dayjs().add(-5, "day").format("YYYY-MM-DD"),
  proRatedLeaseEndDate: dayjs().add(-1, "day").format("YYYY-MM-DD"),
  rentRecipient: "Evangelos Poneres",
  rentRecipientAddress: "PO Box 11, Park Ridge, IL 60068",
  occupyingPersons: "Snoopy",
  numKeys: 2,
  monthToMonthTerminationDaysPriorToEndOfLeaseTerm: 60,
  monthToMonthTerminationDaysPriorToFinalRentalMonth: 60,
  // yearToYearNotice: 60,
  noticeName: "Evangelos Poneres",
  noticeAddress: "PO Box 11, Park Ridge, IL 60068",
  noticePhone: "444 555 1234",
  sendNoticeTo: "yes",
  authorizedToHavePets: "yes",
  petsAllowedNumber: 1,
  petsAllowedType: "Mutt",
  petDepositInDollars: 450,
  authorizedToHaveServiceAnimal: "no",
  serviceAnimalsAllowedNumber: 2,
  serviceAnimalsAllowedType: "Hound",
  petsAllowedWeight: 55,
  furnishedByTenant_1: true,
  furnishedByTenant_2: true,
  furnishedByTenant_3: true,
  furnishedByTenant_4: true,
  furnishedByTenant_5: true,
  furnishedByTenant_6: true,
  furnishedByLandlord_7: true,
  oilPropane: "N/A",
  additionalMaintenance: "N/A",
  addendum: "Sample",
  federalAgeClassification: "no",
  stateAgeClassification: "no",
};
</script>
<style scoped>
.inclusions .checkbox {
  display: inline;
}

.utilities .checkbox {
  display: inline;
  margin: 12px;
}
</style>
