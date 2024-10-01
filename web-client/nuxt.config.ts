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
  runtimeConfig: {
    public: {
      buildDate: process.env.BUILD_DATE || "local",
      deploymentType: process.env.DEPLOYMENT_TYPE || "local",
    },
  },
  nitro: {
    prerender: {
      ignore: ["/logout"],
    },
  },
});
