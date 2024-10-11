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
  return $fetch('/api/agreements', {
    method: "POST",
    body: data,
  });
};

export { previewAgreement, saveAgreement };
