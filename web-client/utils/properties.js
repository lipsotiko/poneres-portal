const saveProperty = async (data) => {
  return $fetch("/api/properties", {
    method: "POST",
    body: data,
  });
};

const getPropertiesCreatedBy = () => {
  return $fetch("/api/properties/created-by");
};

export { saveProperty, getPropertiesCreatedBy };
