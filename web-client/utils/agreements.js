const previewAgreement = async (type, data) => {
  return $fetch(`/api/agreements/preview?type=${type}`, {
    headers: {
      Accept: "application/pdf",
    },
    method: "POST",
    body: data,
  });
};

export { previewAgreement };
