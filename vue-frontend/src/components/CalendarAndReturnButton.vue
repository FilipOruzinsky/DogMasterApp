<script setup lang="ts">
import { Back } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const date = new Date()

const router = useRouter()

const handleBackClick = () => {
    router.push('/')
}
</script>

<template>
    <div class="calendarAndReturn">
        <div class="current-day">{{date.getDate()}}. {{date.getMonth()}}. {{date.getFullYear()}}</div>
        <el-button type="primary">
            <el-icon @click="handleBackClick"><Back /></el-icon>
        </el-button>
    </div>
</template>

<style scoped></style>
