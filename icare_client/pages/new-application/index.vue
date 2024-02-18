<template>
  <div>
    <LillyCaresFormV1
      showLoadTestData
      :metadata="metadata"
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
        directed_to_seek_enrollment: 1,
        prescription_auto_refill: 1,
        electronic_prescription_or_fax: ["fax_prescription_utilizing_page_9"],
        medications: [
          {
            refill_number: "1 Year",
            quantity_dispensed: 1,
            dispense_as_written: 1,
          },
        ],
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
