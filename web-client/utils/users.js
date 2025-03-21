import { ref } from "vue";

const useAuth = () => {
  const isAdmin = ref(false);
  const isTenant = ref(false);

  useAsyncData(
    "user-role",
    async () => {
      const response = await $fetch("/api/user/profile");
      isAdmin.value = response.roles.includes("ADMIN");
      isTenant.value = response.roles.includes("TENANT");
    },
    {
      server: false,
    },
  );

  return {
    isAdmin,
    isTenant
  };
};

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

export {
  useAuth,
  getUsers,
  getPrescribers,
  getPaymentAccountlink,
  emailValidator,
  saveUserProfile,
  sendVerificationEmail,
};
