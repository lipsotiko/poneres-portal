const previewAgreement = async (type, data) => {
  return $fetch(`/api/agreements/preview?type=${type}`, {
    headers: {
      Accept: "application/pdf",
    },
    method: "POST",
    body: data,
  });
};

const saveAgreement = async (data) => {
  return $fetch("/api/agreements", {
    method: "POST",
    body: data,
  });
};

const getAgreement = async (id) => {
  return $fetch(`/api/agreements/${id}`);
};

const deleteAgreement = async (id) => {
  return $fetch(`/api/agreements/${id}`, {
    method: "DELETE",
  });
};

const sendForSigning = async (id) => {
  return $fetch(`/api/agreements/${id}/send`, {
    method: "POST",
  });
};

const getSignatureStatus = async (id) => {
  return $fetch(`/api/agreements/${id}/status`);
};

const getFileUrl = async (id) => {
  return $fetch(`/api/agreements/${id}/file-url`);
};

export {
  previewAgreement,
  saveAgreement,
  getAgreement,
  deleteAgreement,
  sendForSigning,
  getSignatureStatus,
  getFileUrl,
};