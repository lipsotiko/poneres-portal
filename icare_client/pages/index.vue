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
          <th>Patient First Name</th>
          <th>Patient Last Name</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="application in data._embedded.patientApplications">
          <td>
            <IButton size="sm" :to="`/applications/${application.id}`"
              >View</IButton
            >
          </td>
          <td>{{ application.type }}</td>
          <td>{{ application.metadata.patient_first_name }}</td>
          <td>{{ application.metadata.patient_last_name }}</td>
          <td>
            <DownloadPdf :id="application.id" />
          </td>
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
