<template>
  <Card class="p-6">
    <div class="flex justify-between">
      <h3 class="text-lg font-semibold">Basic Information</h3>
      <Button variant="link" @click="emit('goToStep', 1)">Edit</Button>
    </div>
    <table class="mb-4">
      <tbody>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Name</td>
          <td>{{ values.firstName }} {{ values.middleName }} {{ values.lastName }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">DOB</td>
          <td>{{ values.dob }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Gender</td>
          <td>{{ values.gender }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">NPI</td>
          <td>{{ values.npi }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Location</td>
          <td>{{ values.location }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Citizenship status</td>
          <td>{{ values.citizenshipStatus }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Specialty</td>
          <td>{{ values.specialty }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3 align-top">Home address</td>
          <td>
            <span>{{ values.homeAddress.addressLine1 }}</span
            ><br />
            <span>{{ `${values.homeAddress.city}, ${values.homeAddress.state}, ${values.homeAddress.zip}` }}</span>
          </td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3 align-top">Mailing address</td>
          <td>
            <span>{{ values.mailingAddress.addressLine1 }}</span
            ><br />
            <span>{{
              `${values.mailingAddress.city}, ${values.mailingAddress.state}, ${values.mailingAddress.zip}`
            }}</span>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="flex justify-between">
      <h3 class="text-lg font-semibold">Education</h3>
      <Button variant="link" @click="emit('goToStep', 2)">Edit</Button>
    </div>
    <div>
      <table v-for="e in values.educationDetails" class="mb-4">
        <tbody>
          <tr>
            <td class="w-[200px] font-medium text-right pr-3">School Name</td>
            <td>
              {{ e.schoolName }}
            </td>
          </tr>
          <tr>
            <td class="w-[200px] font-medium text-right pr-3">Graduation Date</td>
            <td>
              {{ e.graduationDate }}
            </td>
          </tr>
          <tr>
            <td class="w-[200px] font-medium text-right pr-3">Degree</td>
            <td>
              {{ degreeTypes.find((t) => t.id === e.degree).label }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="flex justify-between">
      <h3 class="text-lg font-semibold">Work history</h3>
      <Button variant="link" @click="emit('goToStep', 3)">Edit</Button>
    </div>
    <table v-for="e in values.employmentHistory" class="mb-4">
      <tbody>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Employer Name</td>
          <td>
            {{ e.employerName }}
          </td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Start Date</td>
          <td>
            {{ e.startDate }}
          </td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">End Date</td>
          <td>
            {{ e.endDate }}
          </td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3 align-top">Description</td>
          <td>
            {{ e.positionDetails }}
          </td>
        </tr>
      </tbody>
    </table>
    <div class="flex justify-between">
      <h3 class="text-lg font-semibold">Credentials</h3>
      <Button variant="link" @click="emit('goToStep', 4)">Edit</Button>
    </div>
    <table>
      <tbody>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Resume</td>
          <td>
            <span>{{ values.resume.name }}</span>
            <Button variant="link" @click="(e) => downloadFile(e, values.resume)">View</Button>
          </td>
        </tr>
        <tr v-for="l in values.licenseFiles">
          <td class="w-[200px] font-medium text-right pr-3 align-top">License</td>
          <td>
            <ul>
                <li>State: {{ l.state }}</li>
                <li>Lic.#: {{ l.licenseNumber}}</li>
                <li>Exp.: {{ l.expirationDate }}</li>
                <li>{{ l.license.name }} <Button variant="link" @click="(e) => downloadFile(e, l.license)">View</Button> </li>
            </ul>
          </td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Government ID</td>
          <td>
            <span>{{ values.govId.name }}</span>
            <Button variant="link" @click="(e) => downloadFile(e, values.govId)">View</Button>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="flex justify-between">
      <h3 class="text-lg font-semibold">Scheduling</h3>
      <Button variant="link" @click="emit('goToStep', 5)">Edit</Button>
    </div>
    <table class="mb-4">
      <tbody>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Employment</td>
          <td>{{ values.employmentType }}</td>
        </tr>
        <tr>
          <td class="w-[200px] font-medium text-right pr-3">Schedule preferences</td>
          <td>
            {{
              schedulePreferences
                .filter((t) => values.schedulePreferences.includes(t.id))
                .map((t) => t.label)
                .join(", ")
            }}
          </td>
        </tr>
      </tbody>
    </table>
  </Card>
</template>
<script setup>
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";

const { values } = defineProps(["values"]);
const emit = defineEmits(["goToStep"]);

const downloadFile = async (e, file) => {
  e.preventDefault();
  navigateTo(URL.createObjectURL(file), {
    open: {
      target: "_blank",
    },
  });
};
</script>
