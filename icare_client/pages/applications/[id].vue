<template>
  <IContainer>
    <div class="top">
      <h1 v-if="pending">...</h1>
      <h1 v-else>
        {{ data.application.metadata.patient_first_name }}
        {{ data.application.metadata.patient_last_name }}
      </h1>
      <div>
        <IButton size="sm" to="/">Home</IButton>
      </div>
    </div>
    <hr />
    <div v-if="pending">Loading ...</div>
    <ITabs v-else v-model="active" stretch>
      <template #header>
        <ITabTitle for="tab-1">Application</ITabTitle>
        <ITabTitle for="tab-2">Documents</ITabTitle>
        <ITabTitle for="tab-3">Sign</ITabTitle>
        <ITabTitle for="tab-4">Review</ITabTitle>
      </template>
      <ITab name="tab-1">
        <LillyCaresFormV1
          v-if="data.application.type === 'LILLY_CARES_V1'"
          showDelete
          :showLoadTestData="false"
          :id="applicationId"
          :metadata="data.application.metadata"
          :disabled="data.application.signedByPatient"
          @submit="submitFn"
          @delete="deletePatientApplicationFn"
        />
        <BoehringerCaresFormV1
          v-else-if="data.application.type === 'BOEHRINGER_CARES_V1'"
          showDelete
          :showLoadTestData="false"
          :id="applicationId"
          :metadata="data.application.metadata"
          :disabled="data.application.signedByPatient"
          @submit="submitFn"
          @delete="deletePatientApplicationFn"
        />
        <NovoNordiskV1
          v-else-if="data.application.type === 'NOVO_NORDISK_V1'"
          showDelete
          :showLoadTestData="false"
          :id="applicationId"
          :metadata="data.application.metadata"
          :disabled="data.application.signedByPatient"
          @submit="submitFn"
          @delete="deletePatientApplicationFn"
        />
        <span v-else>Unable to load form.</span>
      </ITab>
      <ITab name="tab-2">
        <div class="upload-section">
          <input
            ref="fileInput"
            type="file"
            @change="onFilePickedFn"
            multiple
          />
          <IButton
            size="sm"
            color="primary"
            :disabled="files.length === 0"
            :loading="uploading"
            @click="uploadFn"
            >Upload</IButton
          >
        </div>
        <hr />
        <div>
          <div v-if="patientDocumentsPending">Loading ...</div>
          <ITable v-else>
            <thead>
              <tr>
                <th>File Name Type</th>
                <th>Uploaded By</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="doc in patientDocuments">
                <td>
                  <a
                    href="#"
                    @click="downloadDocumentFn(doc.id, doc.fileName)"
                    >{{ doc.fileName }}</a
                  >
                </td>
                <td>{{ doc.uploadedBy }}</td>
                <td>
                  <IButton size="sm" @click="deletePatientDocumentFn(doc.id)">
                    <template #icon>
                      <i class="fa-solid fa-trash"></i>
                    </template>
                  </IButton>
                </td>
              </tr>
            </tbody>
          </ITable>
        </div>
      </ITab>
      <ITab name="tab-3">
        <IContainer>
          <SignaturePad
            label="Patient Signature"
            type="PATIENT"
            :signature="data.patientSignature"
            @clear="clearPatientSignatureFn"
            @save="savePatientSignatureFn"
          />
          <hr />
          <SignaturePad
            label="Prescriber Signature"
            type="PRESCRIBER"
            :signature="data.prescriberSignature"
            @clear="clearPrescriberSignatureFn"
            @save="savePrescriberSignatureFn"
          />
        </IContainer>
      </ITab>
      <ITab name="tab-4">
        <div class="download-section">
          <div>
            <IToggle v-if="isAdmin" v-model="data.application.submitted" :disabled="!data.application.complete">
              This application has been submitted
            </IToggle>
            <small>**Only complete applications may be submitted</small>
          </div>
        </div>
        <hr />
        <span v-if="loadingPreview">loading...</span>
        <iframe ref="pdfPreview"></iframe>
      </ITab>
    </ITabs>
  </IContainer>
</template>
<script setup>
import { ref } from "vue";
import FileSaver from "file-saver";
import { useToast } from "@inkline/inkline/composables";
const { isAdmin } = useAuth();
const toast = useToast();

const {
  params: { id: applicationId },
} = useRoute();

const router = useRouter();

const active = ref("tab-1");
let files = ref([]);
let fileInput = ref(null);
let uploading = ref(false);
let loadingPreview = ref(false);
let pdfPreview = ref(null);

const {
  pending,
  data,
  refresh: refreshPatientApplication,
} = useFetch(`/api/patient-applications/${applicationId}`, {
  lazy: true,
  server: false,
});

const {
  pending: patientDocumentsPending,
  data: patientDocuments,
  refresh: refreshPatientDocuments,
} = useFetch(`/api/patient-documents/application/${applicationId}/get`, {
  lazy: true,
  server: false,
});

watch(active, async (val) => {
  if (val === "tab-1") {
    await refreshPatientApplication();
  }

  if (val === "tab-4") {
    loadingPreview.value = true;
    await getPatientApplicationPdfPreviewWithSignatures(applicationId).then(
      (blob) => {
        var file = window.URL.createObjectURL(blob);
        pdfPreview.value.src = file;
      },
    );
    loadingPreview.value = false;
  } else {
    pdfPreview.value.src = "";
  }
});

watch(
  data,
  (val) => {
    submittedApplication(applicationId, val.application.submitted);
  },
  {
    deep: true,
  },
);

const submitFn = async (data) => {
  await savePatientApplication({
    ...data,
  });
  await refreshPatientApplication();
};

const onFilePickedFn = (e) => {
  files.value = e.target.files;
};

const uploadFn = async () => {
  let formData = new FormData();
  for (let i = 0; i < files.value.length; i++) {
    formData.append("files", files.value.item(i));
  }

  uploading.value = true;

  await savePatientDocument({ applicationId, formData });

  files.value = [];
  fileInput.value.value = "";
  uploading.value = false;

  toast.show({
    title: "Success",
    message: "Patient documents uploaded!",
    color: "success",
  });

  refreshPatientDocuments();
};

const downloadDocumentFn = (id, fileName) => {
  getPatientDocument(id, fileName).then((blob) => {
    FileSaver.saveAs(blob, fileName);
  });
};

const deletePatientDocumentFn = async (id) => {
  await deletePatientDocument(id);
  await refreshPatientDocuments();
  toast.show({
    title: "Success",
    message: "Patient document deleted!",
    color: "success",
  });
};

const savePatientSignatureFn = async (signature) => {
  await savePatientSignature(applicationId, { signature });
  toast.show({
    title: "Success",
    message: "Patient signed application!",
    color: "success",
  });
};

const clearPatientSignatureFn = async () => {
  await clearPatientSignature(applicationId);
  toast.show({
    title: "Success",
    message: "Patient signature cleared!",
    color: "success",
  });
};

const savePrescriberSignatureFn = async (signature) => {
  await savePrescriberSignature(applicationId, { signature });
  toast.show({
    title: "Success",
    message: "Presciber signed application!",
    color: "success",
  });
};

const clearPrescriberSignatureFn = async () => {
  await clearPrescriberSignature(applicationId);
  toast.show({
    title: "Success",
    message: "Prescriber signature cleared!",
    color: "success",
  });
};

const deletePatientApplicationFn = async () => {
  await deletePatientApplication(applicationId);
  router.push("/");
};
</script>
<style>
iframe {
  width: 100%;
  height: 1000px;
}

.upload-section {
  display: flex;
  justify-content: space-between;
}

.download-section {
  display: flex;
  justify-content: flex-end;
}
</style>
