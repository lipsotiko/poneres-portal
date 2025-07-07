<template>
  <Button variant="destructive" @click="deleteModalVisible = true" :disabled="isNew" :loading="deleting">
    Delete
  </Button>
  <Dialog :open="deleteModalVisible">
    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>Delete agreement?</DialogTitle>
        <DialogDescription> Are you sure you want to delete this agreement? </DialogDescription>
      </DialogHeader>
      <DialogFooter>
        <div class="w-full flex justify-between">
          <Button variant="outline" @click="deleteModalVisible = false">No, keep it.</Button>
          <Button variant="destructive" @click="handleDelete()" :disabled="deleting">
            <Loader2 v-if="deleting" class="w-4 h-4 animate-spin" />
            Yes, delete!
          </Button>
        </div>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
<script setup>
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Loader2 } from "lucide-vue-next";
import { toast } from "vue-sonner";

const deleting = ref(false);
const deleteModalVisible = ref(false);
const { id, isNew } = defineProps(["id", "isNew"]);

const handleDelete = () => {
  deleting.value = true;
  deleteAgreement(id)
    .then(() => {
      navigateTo("/agreements");
    })
    .catch((err) => {
      toast.error(err.data.message);
      deleteModalVisible.value = false;
      deleting.value = false;
    });
};
</script>
