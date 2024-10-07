const saveEntity = async (data) => {
  return $fetch("/api/entities", {
    method: "POST",
    body: data,
  });
};

export { saveEntity };
