<script setup lang="ts">
import { Back } from '@element-plus/icons-vue'

const date = new Date()


</script>

<template>
    <div class="calendarAndReturn">
        <div class="current-day">{{date.getDate()}}. {{date.getMonth()}}. {{date.getFullYear()}}</div>
        <el-button  type="primary">
            <el-icon><Back /></el-icon>
        </el-button>
    </div>
</template>

<style scoped></style>
