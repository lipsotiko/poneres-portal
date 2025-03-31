const signUpUser = async (data) => {
  return $fetch("/api/public/sign-up", {
    method: "POST",
    body: data,
  });
};

const tenantApplication = async (data) => {
  return $fetch("/api/public/sign-up/tenant/apply", {
    method: "POST",
    body: data,
  });
};

export { signUpUser, tenantApplication };
