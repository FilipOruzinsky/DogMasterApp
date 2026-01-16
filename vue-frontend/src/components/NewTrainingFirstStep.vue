<script setup lang="ts">
import "@/css/trainingsButtons.css"

const emit = defineEmits(['trainingSelect'])


</script>

<template>
  <div>
      <el-button class="trainings-buttons ">Individual</el-button>
      <el-button class="trainings-buttons">Group</el-button>
  </div>

</template>

<style scoped>

</style>
