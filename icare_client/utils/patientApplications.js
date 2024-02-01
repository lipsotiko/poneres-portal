const savePatientApplication = async (data) => {
    await $fetch("/api/patient-applications", {
        method: "POST",
        body: data
    });
}

const getPatientApplicationPdfPreview = async (id) => {
    return $fetch(`/api/patient-applications/download/${id}`);
}

const getPatientApplicationPdfPreviewWithSignatures = async (id) => {
    return $fetch(`/api/patient-applications/download/${id}`, {
        query: {
            withPatientSignature: 'true',
            withPrescriberSignature: 'true'
        }
    });
}

export { 
    savePatientApplication,
    getPatientApplicationPdfPreview,
    getPatientApplicationPdfPreviewWithSignatures 
}
