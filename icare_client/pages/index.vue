<template>
  <IContainer>
    <div class="top">
      <h1>Applications</h1>
      <div v-if="admin">
        <IButton circle color="primary" @click="toggleAddApplicationModal">
          <template #icon>
            <IIcon name="ink-plus" />
          </template>
        </IButton>
      </div>
    </div>
    <hr />
    <ITabs v-model="active">
      <template #header>
        <ITabTitle for="tab-1">Pending</ITabTitle>
        <ITabTitle for="tab-2">Complete</ITabTitle>
        <ITabTitle for="tab-3">Submitted</ITabTitle>
      </template>
      <ITab name="tab-1">
        <div v-if="pending">Loading ...</div>
        <div v-else-if="data._embedded.patientApplications.length === 0">No pending applications</div>
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
              <td>
                {{ application.metadata.patient_first_name }}
                {{ application.metadata.patient_last_name }}
              </td>
              <td v-if="application.signedByPatient"><IIcon name="ink-check" /></td>
              <td v-else><IIcon name="ink-times" /></td>
              <td v-if="application.signedByPrescriber">
                <IIcon name="ink-check" />
              </td>
              <td v-else><IIcon name="ink-times" /></td>
            </tr>
          </tbody>
        </ITable>
      </ITab>
      <ITab name="tab-2">
        <div>No complete applications</div>
      </ITab>
      <ITab name="tab-3">
        <div>No submitted applications</div>
      </ITab>
    </ITabs>
  </IContainer>
  <ClientOnly>
    <IModal v-model="addApplicationModal">
      <template #header><strong>Select:</strong></template>
      <IRadioGroup v-model="checked" :options="options" />
      <template #footer>
        <IButton color="primary" @click="handleNavigation">Next</IButton>
      </template>
    </IModal>
  </ClientOnly>
</template>
<script setup>
const { pending, data } = useFetch("/api/patient-applications", {
  lazy: true,
  server: false,
});

const admin = ref(false);
const active = ref("tab-1");
const addApplicationModal = ref(false);
const checked = ref();

onMounted(async () => {
  admin.value = await isAdmin();
})

const options = ref([
  { id: "LILLY_CARES_V1", label: "Lilly Cares" },
  { id: "BOEHRINGER_CARES_V1", label: "Boehringer Ingelheim Cares" },
  { id: "NOVO_NORDISK_V1", label: "Novo Nordisk" },
]);

const router = useRouter();

const toggleAddApplicationModal = () => {
  addApplicationModal.value = true;
};

const handleNavigation = () => {
  router.push(`/new-application?type=${checked.value}`);
};
</script>
