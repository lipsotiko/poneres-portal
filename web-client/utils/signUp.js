const signUpTenant = async (data) => {
  return $fetch("/api/public/sign-up/tenant", {
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

export { signUpTenant, tenantApplication };
