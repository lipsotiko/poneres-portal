// https://nuxt.com/docs/api/configuration/nuxt-config
import tailwindcss from "@tailwindcss/vite";

export default defineNuxtConfig({
  modules: ["@inkline/plugin/nuxt", "dayjs-nuxt"],
  devtools: { enabled: true },
  inkline: {
    globals: {
      colorMode: "light",
    },
  },
  css: ["~/assets/css/main.css"],
  vite: {
    plugins: [tailwindcss()],
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
      crawlLinks: true,
      failOnError: true,
      ignore: ["/logout"],
    },
    // devProxy: {
    //   '/api': {
    //     target: "http://localhost:8080",
    //     changeOrigin: true
    //   }
    // }
  },
});
