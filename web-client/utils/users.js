import { ref } from "vue";

const getPrescribers = async () => {
  return $fetch("/api/user/prescribers");
};

const getPaymentAccountlink = () => {
  return $fetch("/api/user/payment-account-link");
};

const getUsers = async (query) => {
  return $fetch("/api/user", {
    query,
  });
};

const saveUserProfile = (id, data) => {
  return $fetch(`/api/user/${id}`, {
    method: "PATCH",
    body: data,
  });
};

const sendVerificationEmail = () => {
  return $fetch("/api/user/send-verification-email", {
    method: "POST",
  });
};

const emailValidator = (email) => {
  if (email === undefined) {
    return false;
  }
  const isEmail = email
    .toLowerCase()
    .match(
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    );
  return isEmail != null;
};

const loginPage = "https://poneres-portal-erg0gza3d2hwc6dh.eastus2-01.azurewebsites.net/oauth2/authorization/okta";

export {
  loginPage,
  getUsers,
  getPrescribers,
  getPaymentAccountlink,
  emailValidator,
  saveUserProfile,
  sendVerificationEmail,
};
