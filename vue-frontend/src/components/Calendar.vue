<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
    trainingGroup: string
}>()

const value = ref(new Date())
const currentUser = 'Me'

// Structure matches your Training.java: name/type and list of participants
interface TrainingDay {
    trainingName: string;
    participants: string[];
}

const scheduledTrainings = ref<Record<string, TrainingDay>>({
    '2026-01-05': {
        trainingName: 'Obedience',
        participants: ['John Doe', 'Jane Smith'],
    },
    '2026-01-17': {
        trainingName: 'Defense',
        participants: ['Alice Brown', 'Bob Wilson'],
    },
})

const handleJoinTraining = (day: string) => {
    // If no training exists on this day, we create a default one
    if (!scheduledTrainings.value[day]) {
        scheduledTrainings.value[day] = {
            trainingName: props.trainingGroup.toLocaleUpperCase(),
            participants: [],
        }
    }

    const training = scheduledTrainings.value[day]
    const index = training.participants.indexOf(currentUser)

    if (index > -1) {
        training.participants.splice(index, 1)
        // Cleanup: If nobody is left, remove the training entry entirely
        if (training.participants.length === 0) {
            delete scheduledTrainings.value[day]
        }
    } else {
        training.participants.unshift(currentUser)
    }
}
</script>

<template>
    <el-calendar v-model="value">
        <template #date-cell="{ data }">
            <div class="calendar-cell" @click="handleJoinTraining(data.day)">
                <div class="day-number">{{ data.day.split('-').slice(2).join('') }}</div>

                <!-- Only show if the day exists AND has at least one person -->
                <div v-if="scheduledTrainings[data.day]?.participants.length" class="training-info">
                    <div class="training-label">
                        {{ scheduledTrainings[data.day].trainingName }}
                    </div>

                    <!-- Participants horizontal list -->
                    <div class="names-container">
                        <span
                            v-for="(user, index) in scheduledTrainings[data.day].participants"
                            :key="user"
                            :class="['user-name', user === currentUser ? 'is-me' : '']"
                        >
                            {{ user }}{{ index < scheduledTrainings[data.day].participants.length - 1 ? ', ' : '' }}
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
