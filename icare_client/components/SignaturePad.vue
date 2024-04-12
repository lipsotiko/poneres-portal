<template>
  <div class="signature-pad-container">
    <label class="signature-label">
      <strong>{{ label }}</strong>
    </label>
    <div class="signature-pad-wrapper">
      <canvas ref="signaturePadCanvas" class="signature-pad" width="600" height="200" />
    </div>
    <div class="signature-buttons">
      <input ref="fileInput" type="file" :disabled="signed || isUploading" @change="onFilePickedFn" />
      <IButton @click="clear" :disabled="isUploading">Clear</IButton>
      <IButton @click="save" color="primary" :disabled="signed" :loading="isUploading">Save</IButton>
    </div>
    <div class="upload-section">
        <ICheckbox v-if="isUpload" v-model="removeBackground" :disabled="isUploading">Remove background</ICheckbox>
    </div>
    <canvas style="display: none" ref="tmpCanvas" width="600" height="200" />
  </div>
</template>
<script setup>
import SignaturePad from "signature_pad";
import trimCanvas from "trim-canvas";
import imglyRemoveBackground from "@imgly/background-removal"

const props = defineProps(["label", "signature"]);
const isUpload = ref(false);
const isUploading = ref(false);
const removeBackground = ref(false);
const tmpCanvas = ref();
const signaturePadCanvas = ref();
const fileInput = ref();
let signed = ref(false);
let signaturePad;

onMounted(() => {
  signaturePad = new SignaturePad(signaturePadCanvas.value, {
    throttle: 0,
    minWidth: 1,
    maxWidth: 3,
  });

  if (props.signature) {
    signed.value = true;
    signaturePad.fromDataURL(props.signature);
  }
});

const emit = defineEmits(["save", "clear"]);

const clear = () => {
  fileInput.value.value = "";
  isUpload.value = false;
  signed.value = false;
  signaturePad.clear();
  emit("clear");
};

const save = async () => {
  if (signaturePad.isEmpty()) {
    return alert("Please provide a signature first.");
  }

  if (isUpload.value) {
    isUploading.value = true;
    if (removeBackground.value) {
      const removedBgBlob = await imglyRemoveBackground(signaturePadCanvas.value.toDataURL(), {
        model: 'small',
        progress: (key, current, total) => {
          console.log(key, current, total);
        }
      });

      const image = await blobToImage(removedBgBlob);
      const ctx = tmpCanvas.value.getContext('2d', { willReadFrequently: true });
      ctx.drawImage(image, 0, 0);

      const trimmed = trimCanvas(tmpCanvas.value).toDataURL("image/png");
      emit("save", trimmed);
    } else {
      emit("save", signaturePadCanvas.value.toDataURL());
    }
  } else {
    const trimmed = trimCanvas(signaturePadCanvas.value).toDataURL("image/png");
    emit("save", trimmed);
  }
  signed.value = true;
  isUpload.value = false;
  isUploading.value = false;
  fileInput.value.value = "";
};

const blobToImage = async (blob) => {
  return new Promise(resolve => {
    const url = URL.createObjectURL(blob)
    let img = new Image()
    img.onload = () => {
      URL.revokeObjectURL(url)
      resolve(img)
    }
    img.src = url
  })
}

const onFilePickedFn = (e) => {
  isUpload.value = true;
  const file = e.target.files[0];
  const reader = new FileReader();
  reader.onloadend = () => {
    signaturePad.clear();
    signaturePad.fromDataURL(reader.result);
  };
  reader.readAsDataURL(file);
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
  margin-bottom: 22px;
}

.signature-pad {
  position: absolute;
  left: 0;
  top: 0;
  width: 600px;
  height: 200px;
  background-color: white;
  border: solid var(--color-light) 1px;
  border-radius: 4px;
}

.upload-section {
  margin-bottom: 8px;
  padding: 0 26px;
}

.signature-buttons {
  display: flex;
  justify-content: space-around;
  width: 100%;
}
</style>
