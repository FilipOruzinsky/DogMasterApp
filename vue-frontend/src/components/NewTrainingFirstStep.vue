<script setup lang="ts">
import '@/css/trainingsButtons.css'

const emit = defineEmits<{
    (e: 'training-select', trainingGroup: string): void
}>()
</script>

<template>
    <div>
        <el-button class="trainings-buttons" @click="emit('training-select', 'individual')">Individual</el-button>
        <el-button class="trainings-buttons" @click="emit('training-select', 'group')">Group</el-button>
    </div>
</template>
