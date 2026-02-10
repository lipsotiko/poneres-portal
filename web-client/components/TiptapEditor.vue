<template>
  <EditorContent :editor="editor" />
</template>

<script setup>
import { useEditor, EditorContent } from "@tiptap/vue-3";
import StarterKit from "@tiptap/starter-kit";
import { watch } from "vue";

const model = defineModel()

const editor = useEditor({
  content: "", // ⬅️ start empty
  extensions: [StarterKit],
  // Don't render on the server, only on the client after hydration
  immediatelyRender: false,
});

watch(() => model.value, (value) => {
  const isSame = editor.value?.getHTML() === value
  if (!isSame) {
    editor.value?.commands.setContent(value, false)
  }
})
</script>
