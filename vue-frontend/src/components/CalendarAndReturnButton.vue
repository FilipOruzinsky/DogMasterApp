<script setup lang="ts">
import { Back } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const date = new Date()

const router = useRouter()

const handleBackClick = () => {
    router.push('/')
}

const handleDateClick = () => {
    router.push('/calendar')
}
</script>

<template>
    <div class="calendarAndReturn">

       <el-button type ="primary" @click="handleDateClick">
        <div class="current-day">{{date.getDate()}}. {{date.getMonth()}}. {{date.getFullYear()}}</div>
       </el-button>


        <el-button type="primary">
            <el-icon @click="handleBackClick">
                <Back />
            </el-icon>
        </el-button>
    </div>
</template>

<style scoped></style>
