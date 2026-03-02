<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
    trainingType?: string
}>()

const emits = defineEmits<{
    (e: 'date-selected', day: string): void
}>()

const date = ref(new Date())
const currentUser = 'Me'
const selectedDay = ref<string | null>(null)

interface TrainingDay {
    trainingName: string
    participants: string[]
}

const scheduledTrainings = ref<Record<string, TrainingDay>>({
    '2026-02-05': {
        trainingName: 'Obedience',
        participants: ['John Doe', 'Jane Smith'],
    },
    '2026-02-17': {
        trainingName: 'Defense',
        participants: ['Alice Brown', 'Bob Wilson'],
    },
})

const handleJoinTraining = (day: string) => {
    if (!props.trainingType) {
        return
    }
    // Remove "ME" from previously selected day
    if (selectedDay.value && selectedDay.value !== day) {
        const prevTraining = scheduledTrainings.value[selectedDay.value]
        if (prevTraining) {
            const prevIndex = prevTraining.participants.indexOf(currentUser)
            if (prevIndex > -1) {
                prevTraining.participants.splice(prevIndex, 1)
                // Cleanup if empty
                if (prevTraining.participants.length === 0) {
                    delete scheduledTrainings.value[selectedDay.value]
                }
            }
        }
    }

    // Toggle for the clicked day
    if (!scheduledTrainings.value[day]) {
        scheduledTrainings.value[day] = {
            trainingName: props.trainingType.toLocaleUpperCase(),
            participants: [],
        }
    }

    const training = scheduledTrainings.value[day]
    const index = training.participants.indexOf(currentUser)

    if (index > -1) {
        // User is deselecting this day
        training.participants.splice(index, 1)
        if (training.participants.length === 0) {
            delete scheduledTrainings.value[day]
        }
        selectedDay.value = null
    } else {
        if (training.participants.length > 0) {
            // User is selecting this day
            confirm('Would you like to join this training?')
            training.participants.unshift(currentUser)
            selectedDay.value = day
        }
    }
    if (training.participants.length === 0) {
        training.participants.unshift(currentUser)
        selectedDay.value = day
    }

    emits('date-selected', day)
}
</script>

<template>
    <el-calendar v-model="date">
        <template #date-cell="{ data }">
            <div class="calendar-cell" @click="handleJoinTraining(data.day)">
                <div class="day-number">{{ data.day.split('-').slice(2).join('') }}</div>

                <div v-if="scheduledTrainings[data.day]?.participants.length" class="training-info">
                    <div class="training-label">
                        {{ scheduledTrainings[data.day].trainingName }}
                    </div>

                    <div class="names-container">
                        <span
                            v-for="(user, index) in scheduledTrainings[data.day].participants"
                            :key="user"
                            :class="['user-name', user === currentUser ? 'is-me' : '']"
                        >
                            {{ user
                            }}{{
                                index < scheduledTrainings[data.day].participants.length - 1
                                    ? ', '
                                    : ''
                            }}
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
