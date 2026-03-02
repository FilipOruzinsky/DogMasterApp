<script setup lang="ts">
import { ref } from 'vue'
import { useTrainingStore } from '@/store/trainingStore.ts'
import { ME } from '@/constants'
import { getDayNumber } from '@/utils'
import { useMessage } from '@/composables/message.ts'

const trainingStore = useTrainingStore()
const { showWarningMessage } = useMessage()

const props = defineProps<{
    trainingType: string
}>()

const emits = defineEmits<{
    (e: 'date-selected', day: string): void
}>()

const date = ref(new Date())
const selectedDay = ref<string | null>(null)

const handleJoinTraining = (day: string) => {
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    const clickedDate = new Date(day)

    if (clickedDate < today) {
        showWarningMessage('Nie je možné vytvoriť tréning v minulosti.', 'Pozor', 'OK')
        return
    }

    const training = trainingStore.scheduledTrainings[day]

    // Ak deň má iných účastníkov a ja tam nie som — opýtaj sa
    if (training && !trainingStore.isMeInTraining(training) && training.participants.length > 0) {
        if (!confirm('Would you like to join this training?')) return
    }

    // Odhlás ma z predchádzajúceho dňa
    if (selectedDay.value && selectedDay.value !== day) {
        trainingStore.joinTraining(selectedDay.value, props.trainingType)
        selectedDay.value = null
    }

    trainingStore.joinTraining(day, props.trainingType)
    selectedDay.value = trainingStore.scheduledTrainings[day] ? day : null

    emits('date-selected', day)
}

const commaOrEmpty = (day: string, index: number): string => {
    return index < trainingStore.scheduledTrainings[day].participants.length - 1 ? ', ' : ''
}
</script>

<template>
    <el-calendar v-model="date">
        <template #date-cell="{ data }">
            <div class="calendar-cell" @click="handleJoinTraining(data.day)">
                <div class="day-number">{{ getDayNumber(data.day) }}</div>

                <div v-if="trainingStore.hasParticipants(data.day)" class="training-info">
                    <div class="training-label">
                        {{ trainingStore.scheduledTrainings[data.day].trainingName }}
                    </div>

                    <div class="names-container">
                        <span
                            v-for="(user, index) in trainingStore.scheduledTrainings[data.day]
                                .participants"
                            :key="user"
                            :class="['user-name', user === ME ? 'is-me' : '']"
                        >
                            {{ user }}{{ commaOrEmpty(data.day, index) }}
                        </span>
                    </div>
                </div>
            </div>
        </template>
    </el-calendar>
</template>

<style scoped>
.calendar-cell {
    height: 100%;
    width: 100%;
    overflow: hidden;
}

.day-number {
    font-weight: bold;
}

.training-info {
    margin-top: 4px;
}

.training-label {
    font-size: 10px;
    font-weight: bold;
    color: #909399;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    margin-bottom: 2px;
}

.names-container {
    font-size: 12px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: #606266;
}

.is-me {
    color: #409eff;
    font-weight: bold;
}

:deep(.el-calendar-table .el-calendar-day) {
    padding: 6px;
}
</style>
