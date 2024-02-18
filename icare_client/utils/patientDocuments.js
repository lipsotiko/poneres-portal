const savePatientDocument = async ({ applicationId, formData }) => {
  await $fetch(`/api/patient-documents/application/${applicationId}`, {
    method: "POST",
    body: formData,
  });
};

const getPatientDocument = async (documentId) => {
  return await $fetch(`/api/patient-documents/${documentId}`);
};

const deletePatientDocument = async (documentId) => {
  return $fetch(`/api/patient-documents/${documentId}`, {
    method: "DELETE",
  });
};

export { savePatientDocument, getPatientDocument, deletePatientDocument };
