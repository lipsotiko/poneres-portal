<template>
  <div class="signature-pad-container">
    <label class="signature-label">{{ label }}</label>
    <div class="signature-pad-wrapper">
      <canvas
        ref="signaturePadCanvas"
        class="signature-pad"
        width="600"
        height="200"
      >
      </canvas>
    </div>
    <div class="signature-buttons">
      <IButton @click="clear">Clear</IButton>
      <IButton @click="save" color="primary">Save</IButton>
    </div>
  </div>
</template>
<script setup>
import SignaturePad from "signature_pad";

const props = defineProps(["label", "signature"]);
const signaturePadCanvas = ref()

let signaturePad;
onMounted(() => {
  signaturePad = new SignaturePad(signaturePadCanvas.value, { throttle: 0, minWidth: 1, maxWidth: 3 });

  if (props.signature) {
    signaturePad.fromDataURL(props.signature);
  }
});

const emit = defineEmits(["save", "clear"]);

const clear = () => {
  signaturePad.clear();
  emit("clear");
};

const save = () => {
  if (signaturePad.isEmpty()) {
    return alert("Please provide a signature first.");
  }

  emit("save", signaturePad.toDataURL("image/png"));
};
</script>
<style scoped>
.signature-label {
  margin-bottom: 8px;
}
.signature-pad-container {
  display: block;
  width: 600px;
  margin: 0 auto;
}

.signature-pad-wrapper {
  position: relative;
  width: 602px;
  height: 202px;
  -moz-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
  border: solid black 1px;
  margin-bottom: 22px;
}

.signature-pad {
  position: absolute;
  left: 0;
  top: 0;
  width: 600px;
  height: 200px;
  background-color: white;
}

.signature-buttons {
  display: flex;
  justify-content: space-around;
  width: 100%;
}
</style>
