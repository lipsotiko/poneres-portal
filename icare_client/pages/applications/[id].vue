<template>
  <IContainer>
    <div class="top">
      <h1 v-if="pending">...</h1>
      <h1 v-else>
        {{ data.metadata.patient_first_name }}
        {{ data.metadata.patient_last_name }}
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
        <ITabTitle for="tab-3">Preview</ITabTitle>
        <ITabTitle for="tab-4">With Signatures</ITabTitle>
      </template>
      <ITab name="tab-1">
        <LillyCaresFormV1
          v-if="data.type === 'LILLY_CARES_V1'"
          :id="route.params.id"
          :metadata="data.metadata"
          :hideLoadTestData="true"
          @submit="submit"
        />
      </ITab>
      <ITab name="tab-2">
        <div class="upload-section">
          <input ref="file-input" type="file" @change="onFilePicked" multiple />
          <IButton
            size="sm"
            color="primary"
            :disabled="files === null"
            :loading="uploading"
            @click="
              async () => {
                await onUpload();
                await refreshPatientDocuments();
              }
            "
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
              <tr v-for="doc in patientDocuments._embedded.patientDocuments">
                <td>
                  <a href="#" @click="downloadDocument(doc.id, doc.fileName)">{{
                    doc.fileName
                  }}</a>
                </td>
                <td>{{ doc.uploadedBy }}</td>
                <td>
                  <IIcon
                    class="delete-icon"
                    name="ink-times"
                    @click="
                      async () => {
                        await deletePatientDocument(doc.id);
                        await refreshPatientDocuments();
                      }
                    "
                  />
                </td>
              </tr>
            </tbody>
          </ITable>
        </div>
      </ITab>
      <ITab name="tab-3">
        <span v-if="loadingPreview">loading...</span>
        <iframe id="pdf-preview"></iframe>
      </ITab>
      <ITab name="tab-4">
        <span v-if="loadingPreview">loading...</span>
        <iframe id="pdf-preview-with-signatures"></iframe>
      </ITab>
    </ITabs>
  </IContainer>
</template>
<script setup>
const route = useRoute();
const { pending, data } = useFetch(
  `/api/patient-applications/${route.params.id}`,
  {
    lazy: true,
    server: false,
  }
);

const {
  pending: patientDocumentsPending,
  data: patientDocuments,
  refresh: refreshPatientDocuments,
} = useFetch(
  `/api/patient-documents/search/findByApplicationId?applicationId=${route.params.id}`,
  {
    lazy: true,
    server: false,
  }
);
</script>
<script>
import FileSaver from "file-saver";

export default {
  data() {
    return {
      active: "tab-1",
      loadingPreview: false,
      files: null,
      uploading: false,
    };
  },
  watch: {
    active: async function (val) {
      const {
        params: { id },
      } = useRoute();

      if (val === "tab-3") {
        this.loadingPreview = true;
        await getPatientApplicationPdfPreview(id).then((blob) => {
          var file = window.URL.createObjectURL(blob);
          document.querySelector("#pdf-preview").src = file;
        });
        this.loadingPreview = false;
      } else if (val === "tab-4") {
        await getPatientApplicationPdfPreviewWithSignatures(id).then((blob) => {
          var file = window.URL.createObjectURL(blob);
          document.querySelector("#pdf-preview-with-signatures").src = file;
        });
        this.loadingPreview = false;
      } else {
        document.querySelector("#pdf-preview").src = "";
        document.querySelector("#pdf-preview-with-signatures").src = "";
      }
    },
  },
  methods: {
    onFilePicked(e) {
      this.files = e.target.files;
    },
    async onUpload() {
      const {
        params: { id: applicationId },
      } = useRoute();

      let formData = new FormData();
      for (let i = 0; i < this.files.length; i++) {
        formData.append("files", this.files.item(i));
      }

      this.uploading = true;

      await savePatientDocument({ applicationId, formData });

      this.files = null;
      this.$refs["file-input"].value = "";
      this.uploading = false;
    },
    downloadDocument(id, fileName) {
      getPatientDocument(id, fileName).then((blob) => {
        FileSaver.saveAs(blob, fileName);
      });
    },
    async submit(data) {
      await savePatientApplication({
        type: "LILLY_CARES_V1",
        ...data,
      });
    },
  },
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

.delete-icon {
  cursor: pointer;
}
</style>
