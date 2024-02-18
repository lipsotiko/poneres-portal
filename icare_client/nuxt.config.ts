// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  modules: ["@inkline/plugin/nuxt"],
  devtools: { enabled: true },
  inkline: {
    globals: {
      colorMode: "light",
    },
  },
  vite: {
    server: {
      proxy: {
        "/api": {
          target: "http://localhost:8080",
          changeOrigin: true,
        },
      },
    },
  },
});
