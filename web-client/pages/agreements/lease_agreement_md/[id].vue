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
        <div
          v-for="o in [
            { value: 'incl_1', label: 'Alarm System' },
            { value: 'incl_2', label: 'Carbon Monoxide Detector(s)' },
            { value: 'incl_3', label: 'Ceiling Fan(s)' },
            { value: 'incl_4', label: 'Central Vacuum' },
            { value: 'incl_5', label: 'Clothes Dryer' },
            { value: 'incl_6', label: 'Clothes Washer' },
            { value: 'incl_7', label: 'Cooktop' },
            { value: 'incl_8', label: 'Dishwasher' },
            { value: 'incl_9', label: 'Drapery/Curtain Rods' },
            { value: 'incl_10', label: 'Draperies/Curtains' },
            { value: 'incl_11', label: 'Electronic Air Filter' },
            { value: 'incl_12', label: 'Exhaust Fan(s)' },
            { value: 'incl_13', label: 'Exist. w/w Carpet' },
          ]"
        >
          <FormField v-slot="{ value, handleChange }" type="checkbox" :name="o.value">
            <FormControl>
              <Checkbox :model-value="value" @update:model-value="handleChange" />
            </FormControl>
            <FormLabel>{{ o.label }}</FormLabel>
          </FormField>
        </div>
      </div>
      <div>
        <div
          v-for="o in [
            { value: 'incl_14', label: 'Fireplace Screen Doors' },
            { value: 'incl_15', label: 'Freezer' },
            { value: 'incl_16', label: 'Furnace Humidifier' },
            { value: 'incl_17', label: 'Garage Opener(s)' },
            { value: 'incl_18', label: 'w/remote(s)' },
            { value: 'incl_19', label: 'Garbage Disposer' },
            { value: 'incl_20', label: 'Hot Tub, Equip. & Cover' },
            { value: 'incl_21', label: 'Intercom' },
            { value: 'incl_22', label: 'Lawn Mowers(s)' },
            { value: 'incl_23', label: 'Microwave' },
            { value: 'incl_24', label: 'Playground Equipment' },
            { value: 'incl_25', label: 'Pool, Equip. & Cover' },
            { value: 'incl_26', label: 'Refrigerator(s)' },
            { value: 'incl_27', label: 'w/ice maker' },
          ]"
        >
          <FormField v-slot="{ value, handleChange }" type="checkbox" :name="o.value">
            <FormControl>
              <Checkbox :model-value="value" @update:model-value="handleChange" />
            </FormControl>
            <FormLabel>{{ o.label }}</FormLabel>
          </FormField>
        </div>
      </div>
      <div>
        <div
          v-for="o in [
            { value: 'incl_28', label: 'Satellite Dish' },
            { value: 'incl_29', label: 'Screens' },
            { value: 'incl_30', label: 'Shades/Blinds' },
            { value: 'incl_31', label: 'Storage Shed(s)' },
            { value: 'incl_32', label: 'Storm Doors' },
            { value: 'incl_33', label: 'Storm Windows' },
            { value: 'incl_34', label: 'Stove or Range' },
            { value: 'incl_35', label: 'T.V. Antenna' },
            { value: 'incl_36', label: 'Trash Compactor' },
            { value: 'incl_37', label: 'Wall Oven(s)' },
            { value: 'incl_38', label: 'Water Filter' },
            { value: 'incl_39', label: 'Water Softener' },
            { value: 'incl_40', label: 'Window A/C Unit(s)' },
          ]"
        >
          <FormField v-slot="{ value, handleChange }" type="checkbox" :name="o.value">
            <FormControl>
              <Checkbox :model-value="value" @update:model-value="handleChange" />
            </FormControl>
            <FormLabel>{{ o.label }}</FormLabel>
          </FormField>
        </div>
      </div>
      <div>
        <div
          v-for="o in [
            { value: 'incl_41', label: 'Window Fan(s)' },
            { value: 'incl_42', label: 'Wood Stove' },
          ]"
        >
          <FormField v-slot="{ value, handleChange }" type="checkbox" :name="o.value">
            <FormControl>
              <Checkbox :model-value="value" @update:model-value="handleChange" />
            </FormControl>
            <FormLabel>{{ o.label }}</FormLabel>
          </FormField>
        </div>
      </div>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="additionalInclusions">
          <FormItem>
            <FormLabel>Additonal inclusions</FormLabel>
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
        <FormField v-slot="{ componentField }" name="additionalExclusions">
          <FormItem>
            <FormLabel>Additonal exclusions</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Utilities</p>
    </div>
    <div class="py-2">
      <div>
        <table class="utilities">
          <tbody>
            <tr
              v-for="(o, idx) in [
                {
                  rowLabel: 'a. Cable TV',
                  checkboxes: [
                    {
                      value: 'furnishedByLandlord_1',
                      label: 'Landlord',
                    },
                    {
                      value: 'furnishedByTenant_1',
                      label: 'Tenant',
                    },
                  ],
                }, {
                  rowLabel: 'b. Cold Water/Sewer',
                  checkboxes: [
                  {
                      value: 'furnishedByLandlord_2',
                      label: 'Landlord',
                    },
                    {
                      value: 'furnishedByTenant_2',
                      label: 'Tenant',
                    },
                  ]
                }, {
                  rowLabel: 'c. Cooking Fuel',
                  checkboxes: [
                  {
                      value: 'furnishedByLandlord_3',
                      label: 'Landlord',
                    },
                    {
                      value: 'furnishedByTenant_3',
                      label: 'Tenant',
                    },
                  ]
                }, {
                  rowLabel: 'd. Electricity',
                  checkboxes: [
                  {
                      value: 'furnishedByLandlord_4',
                      label: 'Landlord',
                    },
                    {
                      value: 'furnishedByTenant_4',
                      label: 'Tenant',
                    },
                  ]
                }, {
                  rowLabel: 'e. Heating Fuel',
                  checkboxes: [
                  {
                      value: 'furnishedByLandlord_5',
                      label: 'Landlord',
                    },
                    {
                      value: 'furnishedByTenant_5',
                      label: 'Tenant',
                    },
                  ]
                }, {
                  rowLabel: 'f. Heating of Water',
                  checkboxes: [
                  {
                      value: 'furnishedByLandlord_6',
                      label: 'Landlord',
                    },
                    {
                      value: 'furnishedByTenant_6',
                      label: 'Tenant',
                    },
                  ]
                }, {
                  rowLabel: 'g. Trash Removal',
                  checkboxes: [
                  {
                      value: 'furnishedByLandlord_7',
                      label: 'Landlord',
                    },
                    {
                      value: 'furnishedByTenant_7',
                      label: 'Tenant',
                    },
                  ]
                }
              ]"
            >
              <td>{{ o.rowLabel }}</td>
              <td>
                <FormField v-slot="{ value, handleChange }" type="checkbox" :name="`furnishedByLandlord_${idx+1}`">
                  <FormControl>
                    <Checkbox :model-value="value" @update:model-value="handleChange" />
                  </FormControl>
                  <FormLabel>Landlord</FormLabel>
                </FormField>
              </td>
              <td>
                <FormField v-slot="{ value, handleChange }" type="checkbox" :name="`furnishedByTenant_${idx+1}`">
                  <FormControl>
                    <Checkbox :model-value="value" @update:model-value="handleChange" />
                  </FormControl>
                  <FormLabel>Tenant</FormLabel>
                </FormField>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="py-2">
        <FormField v-slot="{ componentField }" name="oilPropane">
          <FormItem>
            <FormLabel>In the event the Property uses oil/propane for heat:</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Maintenance</p>
    </div>
    <div class="grid grid-cols-2 gap-4 py-2 inclusions">
      <div>
        <FormField v-slot="{ componentField }" name="additionalMaintenance">
          <FormItem>
            <FormLabel>Additonal maintenance</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="poolCompany">
          <FormItem>
            <FormLabel>Pool company</FormLabel>
            <FormControl>
              <Input v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Showing of property</p>
    </div>
    <div class="grid grid-cols-3 gap-3 py-2">
      <div>
        <FormField v-slot="{ componentField }" name="showingDays">
          <FormItem>
            <FormLabel>Days prior to end of lease</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="showingStartTime">
          <FormItem>
            <FormLabel>Showing start time</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="time" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
      <div>
        <FormField v-slot="{ componentField }" name="showingEndTime">
          <FormItem>
            <FormLabel>Showing end time</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="time" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Age classification of property</p>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="federalAgeClassification">
          <FormItem>
            <FormLabel>The Federal Program (check one)</FormLabel>
            <FormControl>
              <RadioGroup v-bind="componentField">
                <div
                  v-for="o in [
                    {
                      id: 'no',
                      label: 'the Property was built during or after 1978; the Federal Program does not apply',
                    },
                    { id: 'yes', label: 'the Property was built before 1978; the Federal Program applies' },
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
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="stateAgeClassification">
          <FormItem>
            <FormLabel>The State Program (check one)</FormLabel>
            <FormControl>
              <RadioGroup v-bind="componentField">
                <div
                  v-for="o in [
                    { id: 'yes', label: 'the Property was built prior to 1978; the Maryland Program applies.' },
                    {
                      id: 'no',
                      label: 'the Property was built during or after 1978; the Maryland Program does not apply.',
                    },
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
      <p class="font-semibold">Additional provisions</p>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="additionalProvisions">
          <FormItem>
            <FormControl>
              <Textarea v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Addendum(s) attched concerning</p>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="addendumAttachedConcerning">
          <FormItem>
            <FormControl>
              <Textarea v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <div class="mt-4">
      <p class="font-semibold">Addendum</p>
    </div>
    <div class="py-2">
      <div>
        <FormField v-slot="{ componentField }" name="addendum">
          <FormItem>
            <FormControl>
              <Textarea v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
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
  incl_1: z.boolean().optional(),
  incl_2: z.boolean().optional(),
  incl_3: z.boolean().optional(),
  incl_4: z.boolean().optional(),
  incl_5: z.boolean().optional(),
  incl_6: z.boolean().optional(),
  incl_7: z.boolean().optional(),
  incl_8: z.boolean().optional(),
  incl_9: z.boolean().optional(),
  incl_10: z.boolean().optional(),
  incl_11: z.boolean().optional(),
  incl_12: z.boolean().optional(),
  incl_13: z.boolean().optional(),
  incl_14: z.boolean().optional(),
  incl_15: z.boolean().optional(),
  incl_16: z.boolean().optional(),
  incl_17: z.boolean().optional(),
  incl_18: z.boolean().optional(),
  incl_19: z.boolean().optional(),
  incl_20: z.boolean().optional(),
  incl_21: z.boolean().optional(),
  incl_22: z.boolean().optional(),
  incl_23: z.boolean().optional(),
  incl_24: z.boolean().optional(),
  incl_25: z.boolean().optional(),
  incl_26: z.boolean().optional(),
  incl_27: z.boolean().optional(),
  incl_28: z.boolean().optional(),
  incl_29: z.boolean().optional(),
  incl_30: z.boolean().optional(),
  incl_31: z.boolean().optional(),
  incl_32: z.boolean().optional(),
  incl_33: z.boolean().optional(),
  incl_34: z.boolean().optional(),
  incl_35: z.boolean().optional(),
  incl_36: z.boolean().optional(),
  incl_37: z.boolean().optional(),
  incl_38: z.boolean().optional(),
  incl_39: z.boolean().optional(),
  incl_40: z.boolean().optional(),
  incl_41: z.boolean().optional(),
  incl_42: z.boolean().optional(),
  additionalInclusions: z.string().optional(),
  additionalExclusions: z.string().optional(),
  furnishedByLandlord_1: z.boolean().optional(),
  furnishedByTenant_1: z.boolean().default(true).optional(),
  furnishedByLandlord_2: z.boolean().optional(),
  furnishedByTenant_2: z.boolean().default(true).optional(),
  furnishedByLandlord_3: z.boolean().optional(),
  furnishedByTenant_3: z.boolean().default(true).optional(),
  furnishedByLandlord_4: z.boolean().optional(),
  furnishedByTenant_4: z.boolean().default(true).optional(),
  furnishedByLandlord_5: z.boolean().optional(),
  furnishedByTenant_5: z.boolean().default(true).optional(),
  furnishedByLandlord_6: z.boolean().optional(),
  furnishedByTenant_6: z.boolean().default(true).optional(),
  furnishedByLandlord_7: z.boolean().default(true).optional(),
  furnishedByTenant_7: z.boolean().optional(),
  oilPropane: z.string().optional(),
  additionalMaintenance: z.string().optional(),
  showingDays: z.string(),
  showingStartTime: z.string(),
  showingEndTime: z.string(),
  poolCompany: z.string().optional(),
  federalAgeClassification: z.string(),
  stateAgeClassification: z.string(),
  // ageUnknown: { ...fieldOptions, value: false },
  additionalProvisions: z.string().optional(),
  addendumAttachedConcerning: z.string().optional(),
  addendum: z.string().optional(),
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
  securityDepositInDollars: 1234,
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
  incl_5: true,
  incl_6: true,
  incl_8: true,
  incl_23: true,
  incl_26: true,
  incl_29: true,
  incl_30: true,
  incl_34: true,
  additionalInclusions: "N/A",
  additionalExclusions: "N/A",
  furnishedByTenant_1: true,
  furnishedByTenant_2: true,
  furnishedByTenant_3: true,
  furnishedByTenant_4: true,
  furnishedByTenant_5: true,
  furnishedByTenant_6: true,
  furnishedByLandlord_7: true,
  oilPropane: "N/A",
  additionalMaintenance: "N/A",
  poolCompany: "N/A",
  showingDays: 30,
  showingStartTime: "09:00",
  showingEndTime: "20:00",
  additionalProvisions: "Utilities are to be kept under tenant's name for the duration of the lease",
  addendumAttachedConcerning: "N/A",
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
