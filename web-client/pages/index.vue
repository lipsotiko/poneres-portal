<template>
  <IContainer>
    <div class="top">
      <h1>Applications</h1>
      <div v-if="isAdmin" class="admin-section">
        <ISelect
          v-model="selectedPrescriber"
          :options="prescriberOptions"
          size="sm"
          placeholder="Choose a prescriber..."
          clearable
        />
        <IButton
          circle
          color="primary"
          @click="toggleAddApplicationModal"
          :disabled="selectedPrescriber === null"
        >
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
      <div v-if="pending">Loading ...</div>
      <div v-else-if="data?.content.length === 0">{{ emptyResults() }}</div>
      <ITable v-else>
        <thead>
          <tr>
            <th></th>
            <th>Patient Name</th>
            <th>Application Type</th>
            <th>All Documents Uploaded</th>
            <th>Signed By Patient</th>
            <th>Signed By Prescriber</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="application in data?.content">
            <td>
              <IButton size="sm" :to="`/applications/${application.id}`">
                View
              </IButton>
            </td>
            <td>
              {{ application.metadata.patient_first_name }}
              {{ application.metadata.patient_last_name }}
            </td>
            <td>{{ application.displayApplicationName }}</td>
            <td >
              <IIcon v-if="application.allDocumentsUploaded" name="ink-check" />
              <IIcon v-else name="ink-times" />
            </td>
            <td>
              <IIcon v-if="application.signedByPatient" name="ink-check" />
              <IIcon v-else name="ink-times" />
            </td>
            <td>
              <IIcon v-if="application.signedByPrescriber" name="ink-check" />
              <IIcon v-else name="ink-times" />
            </td>
          </tr>
        </tbody>
      </ITable>
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
const router = useRouter();
const addApplicationModal = ref(false);
const checked = ref();
const active = ref("tab-1");
const prescriberOptions = ref([]);
const selectedPrescriber = ref(null);
const { isAdmin } = useAuth();

const { pending, data } = await useAsyncData(
  'patient-applications',
  () =>
    $fetch('/api/patient-applications/find', {
      query: {
        complete: active.value === 'tab-2',
        submitted: active.value === 'tab-3',
        email: selectedPrescriber.value,
      }
    }),
  {
    server: false,
    watch: [selectedPrescriber, active],
  }
);

onMounted(async () => {
  if (isAdmin) {
    prescriberOptions.value = await getPrescribers().then((results) =>
      results.map(({ email, firstName, lastName }) => ({
        id: email,
        label: `${lastName}, ${firstName}`,
      })),
    );
  }
});

const options = ref([
  { id: "LILLY_CARES_V1", label: "Lilly Cares" },
  { id: "BOEHRINGER_CARES_V1", label: "Boehringer Ingelheim Cares" },
  { id: "NOVO_NORDISK_V1", label: "Novo Nordisk" },
]);

const toggleAddApplicationModal = () => {
  addApplicationModal.value = true;
};

const handleNavigation = () => {
  const encodedPrescriberEmail = selectedPrescriber.value;
  router.push({
    name: "new-application",
    query: { type: checked.value, prescriberEmail: encodedPrescriberEmail },
  });
};

const emptyResults = () => {
  if (active.value === 'tab-1') {
    return 'No pending applications'
  }

  if (active.value === 'tab-2') {
    return 'No complete applications'
  }

  if (active.value === 'tab-3') {
    return 'No submitted applications'
  }
}

</script>
<style>
.admin-section {
  display: flex;
}

.admin-section .input {
  margin: 4px 22px;
}
</style>
