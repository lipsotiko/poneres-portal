<template>
  <IContainer>
    <div class="top">
      <h1>Applications</h1>
      <div>
        <IButton circle to="/new-application" color="primary">
          <template #icon>
            <IIcon name="ink-plus" />
          </template>
        </IButton>
      </div>
    </div>
    <hr />
    <div v-if="pending">Loading ...</div>
    <ITable v-else>
      <thead>
        <tr>
          <th></th>
          <th>Application Type</th>
          <th>Patient Name</th>
          <th>Signed By Patient</th>
          <th>Signed By Prescriber</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="application in data._embedded.patientApplications">
          <td>
            <IButton size="sm" :to="`/applications/${application.id}`">
              View
            </IButton>
          </td>
          <td>{{ application.displayApplicationName }}</td>
          <td>{{ application.metadata.patient_first_name }} {{ application.metadata.patient_last_name }}</td>
          <td v-if="application.signedByPatient"><IIcon name="ink-check" /></td>
          <td v-else><IIcon name="ink-times" /></td>
          <td v-if="application.signedByPrescriber"><IIcon name="ink-check" /></td>
          <td v-else><IIcon name="ink-times" /></td>
        </tr>
      </tbody>
    </ITable>
  </IContainer>
</template>
<script setup>
const { pending, data } = useFetch("/api/patient-applications", {
  lazy: true,
  server: false,
});
</script>
