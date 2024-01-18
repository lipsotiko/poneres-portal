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
        <ITabTitle for="tab-2">Preview</ITabTitle>
        <ITabTitle for="tab-3"> Tab 3 </ITabTitle>
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
        <span v-if="loadingPreview">loading...</span>
        <iframe id="pdf-preview"></iframe>
      </ITab>
      <ITab name="tab-3"> Tab 3 content </ITab>
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
</script>
<script>
export default {
  data() {
    return {
      active: "tab-1",
      loadingPreview: false,
    };
  },
  watch: {
    active: async function (val) {
      if (val === "tab-2") {
        this.loadingPreview = true;
        const {
          params: { id },
        } = useRoute();
        await getPatientApplicationPdfPreview(id).then((blob) => {
          var file = window.URL.createObjectURL(blob);
          document.querySelector("#pdf-preview").src = file;
        });
        this.loadingPreview = false;
      } else {
        document.querySelector("#pdf-preview").src = "";
      }
    },
  },
  methods: {
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
</style>
