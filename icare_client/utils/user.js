const isAdmin = async () => {
    const response = await $fetch("/api/user/role");
    return response.type === 'ADMIN';
}

export {
    isAdmin
}
