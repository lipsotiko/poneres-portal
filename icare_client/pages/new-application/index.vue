<template>
  <div>
    <LillyCaresFormV1
      v-if="type === 'LILLY_CARES_V1'"
      showLoadTestData
      :metadata="metadata"
      @submit="submit"
      @loadTestData="loadTestData"
    />
    <BoehringerCaresFormV1
      v-else-if="type === 'BOEHRINGER_CARES_V1'"
      showLoadTestData
      :metadata="metadata"
      @submit="submit"
      @loadTestData="loadTestData"
    />
    <span v-else>Unable to load form.</span>
  </div>
</template>
<script setup>
const {
  query: { type },
} = useRoute();
</script>
<script>
export default {
  data() {
    const {
      query: { type },
    } = useRoute();

    if (type === 'LILLY_CARES_V1') {
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
    } else {
      return {
        metadata: {}
      }
    }
  },
  methods: {
    loadTestData(metadata) {
      this.metadata = metadata;
    },
    async submit() {
      await savePatientApplication({
        type: this.$route.query.type,
        metadata: this.metadata,
      });
      this.$router.push({ path: "/" });
    },
  },
};
</script>
