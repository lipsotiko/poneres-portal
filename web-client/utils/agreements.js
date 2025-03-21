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

const copyAgreement = async (id) => {
  return $fetch(`/api/agreements/${id}/copy`, {
    method: 'POST'
  });
};

const sendReminder = async (id) => {
  return $fetch(`/api/agreements/${id}/send-reminder`, {
    method: "POST",
  });
};

const isTestMode = async () => {
  return $fetch('/api/agreements/test-mode');
}

const saveMaintenanceRequest = async (data) => {
  return $fetch("/api/maintenance", {
    method: "POST",
    body: data,
  });
};

const agreementTypes = [
  {
    id: "/agreements/lease_agreement_md/",
    label: "Residential Dwelling Lease (Maryland)",
    type: "LEASE_AGREEMENT_MD_V1"
  },
  {
    id: "/agreements/lead_paint_disclosure_md/",
    label: "Lead Paint Disclosure (Maryland)",
    type: "LEAD_PAINT_ADDENDUM_MD_V1"
  },
  {
    id: "/agreements/property_management_agreement/",
    label: "Property Management Agreement",
    type: "PROPERTY_MANAGEMENT_AGREEMENT_V1"
  }
];

const getAgreementPath = (type) => {
  return agreementTypes.find(t => t.type === type).id
};

const errorTypes = ["touched", "invalid"];
const fieldOptions = {
  validators: [
    {
      name: "required",
    },
  ],
};

const emailFieldOptions = {
  validators: [
    {
      name: "required",
    },
    {
      name: "custom",
      message: "Please enter a valid email address.",
      validator: emailValidator,
    },
  ],
};

export {
  previewAgreement,
  saveAgreement,
  getAgreement,
  deleteAgreement,
  sendForSigning,
  getSignatureStatus,
  getFileUrl,
  sendReminder,
  getAgreementPath,
  isTestMode,
  copyAgreement,
  errorTypes,
  fieldOptions,
  emailFieldOptions,
  agreementTypes
};
