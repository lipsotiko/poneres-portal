import { ref } from "vue";

const useAuth = () => {
  const isAdmin = ref(false);

  useAsyncData(
    'user-role',
    async () => {
      const response = await $fetch('/api/user/role')
      isAdmin.value = response.type === 'ADMIN'
    }, {
      server: false,
    }
  );

  return {
    isAdmin,
  };
};

const getPrescribers = async () => {
  return $fetch('/api/user-profile/prescribers');
};

export { useAuth, getPrescribers };
