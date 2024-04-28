import { ref } from "vue";

const useAuth = () => {
  const isAdmin = ref(false);
  fetch("/api/user/role")
    .then((res) => res.json())
    .then((json) => {
      isAdmin.value = json.type === "ADMIN";
    });

  return {
    isAdmin,
  };
};

const getPrescribers = async () => {
  return $fetch("/api/user-profile/prescribers");
};

export { useAuth, getPrescribers };
