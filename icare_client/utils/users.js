const isAdmin = async () => {
    const response = await $fetch("/api/user/role");
    return response.type === 'ADMIN';
}

const getPrescribers = async () => {
    return $fetch('/api/user-profile/prescribers');
}

export {
    isAdmin,
    getPrescribers
}
