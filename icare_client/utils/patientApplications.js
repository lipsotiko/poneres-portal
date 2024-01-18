const savePatientApplication = async (data) => {
    await $fetch("/api/patient-applications", {
        method: "POST",
        body: data
    });
}

const getPatientApplicationPdfPreview = async (id) => {
    return $fetch(`/api/patient-applications/download/${id}`);
}

export { getApplications, savePatientApplication, getPatientApplicationPdfPreview }
