<script setup lang="ts">
import { List } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import Logout from '@/components/Logout.vue'
import { useTrainingStore } from '@/store/trainingStore.ts'

const trainingStore = useTrainingStore()

const router = useRouter()

const handleScheduleTraining = () => {
    router.push('/new-training')
}
</script>

<template>
    <div>
        <div class="trainings">
            <div class="done">Trainings done {{ trainingStore.doneTrainings }}</div>
            <div class="scheduled">Scheduled trainings {{ trainingStore.scheduledCount }}</div>
        </div>
        <div class="action-buttons">
            <el-button type="primary" @click="handleScheduleTraining">
                Schedule training
                <el-icon class="el-icon--right">
                    <List />
                </el-icon>
            </el-button>
            <Logout />
        </div>
    </div>
</template>

<style scoped></style>
