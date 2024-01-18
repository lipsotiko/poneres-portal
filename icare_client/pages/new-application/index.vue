<template>
  <div>
    <LillyCaresFormV1
      :metadata="metadata"
      :hideLoadTestData="false"
      @submit="submit"
      @loadTestData="loadTestData"
    />
  </div>
</template>
<script>
export default {
  data() {
    return {
      metadata: {
        medications: [{}],
      },
    };
  },
  methods: {
    loadTestData(metadata) {
      this.metadata = metadata;
    },
    async submit() {
      await savePatientApplication({
        type: "LILLY_CARES_V1",
        metadata: this.metadata,
      });
      this.$router.push({ path: "/" });
    },
  },
};
</script>
