const saveMaintenanceRequest = async (data) => {
    return $fetch("/api/maintenance", {
      method: "POST",
      body: data,
    });
  };

export {
    saveMaintenanceRequest
}
