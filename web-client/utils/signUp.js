const signUpLandlord = async (data) => {
    return $fetch("/api/public/sign-up/landlord", {
      method: "POST",
      body: data,
    });
  };

export {
    signUpLandlord
}
