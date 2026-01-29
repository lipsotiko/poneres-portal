<template>
  <EditorContent :editor="editor" />
</template>

<script setup>
import { useEditor, EditorContent } from "@tiptap/vue-3";
import StarterKit from "@tiptap/starter-kit";

const editor = useEditor({
  content: `<p>I'm running <b>Tiptap</b> with Vue.js. 🎉</p>`,
  extensions: [StarterKit],
  // Don't render on the server, only on the client after hydration
  immediatelyRender: false,
});
</script>
