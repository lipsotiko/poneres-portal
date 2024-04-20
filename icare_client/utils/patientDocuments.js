const savePatientDocument = async ({ applicationId, formData }) => {
  await $fetch(`/api/patient-documents/application/${applicationId}/save`, {
    method: "POST",
    body: formData,
  });
};

const getPatientDocument = async (documentId) => {
  return await $fetch(`/api/patient-documents/${documentId}/download`);
};

const deletePatientDocument = async (documentId) => {
  return $fetch(`/api/patient-documents/${documentId}/delete`, {
    method: "DELETE",
  });
};

export { savePatientDocument, getPatientDocument, deletePatientDocument };
