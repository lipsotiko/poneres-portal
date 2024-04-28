const getPatientApplications = (email) => {
  return $fetch("/api/patient-applications/find", {
    query: {
      email,
      page: 0,
      pageSize: 10,
    },
  });
};

const savePatientApplication = async (data) => {
  await $fetch("/api/patient-applications/save", {
    method: "POST",
    body: data,
  });
};

const savePatientSignature = async (applicationId, patientSignature) => {
  await $fetch(`/api/patient-applications/patient-signature/${applicationId}`, {
    method: "POST",
    body: patientSignature,
  });
};

const previousSignatures = async (type) => {
  return $fetch(`/api/patient-applications/previous-signatures/${type}`);
};

const clearPatientSignature = async (applicationId) => {
  await $fetch(
    `/api/patient-applications/patient-signature/${applicationId}/clear`,
    {
      method: "POST",
    },
  );
};

const savePrescriberSignature = async (applicationId, prescriberSignature) => {
  await $fetch(
    `/api/patient-applications/prescriber-signature/${applicationId}`,
    {
      method: "POST",
      body: prescriberSignature,
    },
  );
};

const clearPrescriberSignature = async (applicationId) => {
  await $fetch(
    `/api/patient-applications/prescriber-signature/${applicationId}/clear`,
    {
      method: "POST",
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

const submittedApplication = async (applicationId, clear) => {
  await $fetch(`/api/patient-applications/${applicationId}/submitted`, {
    method: "POST",
    query: {
      clear,
    },
  });
};

export {
  submittedApplication,
  getPatientApplications,
  previousSignatures,
  savePatientApplication,
  deletePatientApplication,
  savePatientSignature,
  savePrescriberSignature,
  clearPatientSignature,
  clearPrescriberSignature,
  getPatientApplicationPdfPreview,
  getPatientApplicationPdfPreviewWithSignatures,
};
