const signUpLandlord = async (data) => {
    await $fetch("/api/public/sign-up/landlord", {
      method: "POST",
      body: data,
    });
  };

export {
    signUpLandlord
}
