const savePatientApplication = async (data) => {
  await $fetch("/api/patient-applications", {
    method: "POST",
    body: data,
  });
};

const savePatientSignature = async (applicationId, patientSignature) => {
  await $fetch(
    `/api/patient-applications/save-patient-signature/${applicationId}`,
    {
      method: "POST",
      body: patientSignature,
    },
  );
};

const savePrescriberSignature = async (applicationId, prescriberSignature) => {
  await $fetch(
    `/api/patient-applications/save-prescriber-signature/${applicationId}`,
    {
      method: "POST",
      body: prescriberSignature,
    },
  );
};

const deletePatientApplication = async (applicationId) => {
  await $fetch(`/api/patient-applications/${applicationId}`, {
    method: "DELETE",
  });
};

const getPatientApplicationPdfPreview = async (id) => {
  return $fetch(`/api/patient-applications/download/${id}`);
};

const getPatientApplicationPdfPreviewWithSignatures = async (id) => {
  return $fetch(`/api/patient-applications/download/${id}`);
};

export {
  savePatientApplication,
  savePatientSignature,
  savePrescriberSignature,
  deletePatientApplication,
  getPatientApplicationPdfPreview,
  getPatientApplicationPdfPreviewWithSignatures,
};
