import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

interface TrainingDay {
    trainingName: string
    participants: string[]
}

export const useTrainingStore = defineStore('training', () => {
    const currentUser = 'Me'

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

    const doneTrainings = computed(() => {
        const today = new Date()

        return Object.entries(scheduledTrainings.value).filter(([date, training]) => {
            const trainingDate = new Date(date)
            return trainingDate < today && training.participants.includes(currentUser)
        }).length
    })

    const scheduledCount = computed(() => {
        const today = new Date()

        return Object.entries(scheduledTrainings.value).filter(([date, training]) => {
            const trainingDate = new Date(date)
            return trainingDate >= today && training.participants.includes(currentUser)
        }).length
    })

    function joinTraining(day: string, trainingType: string) {
        if (!scheduledTrainings.value[day]) {
            scheduledTrainings.value[day] = {
                trainingName: trainingType.toUpperCase(),
                participants: [],
            }
        }

        const training = scheduledTrainings.value[day]
        const index = training.participants.indexOf(currentUser)

        if (index > -1) {
            // Odhlásenie z tréningu
            training.participants.splice(index, 1)
            if (training.participants.length === 0) {
                delete scheduledTrainings.value[day]
            }
        } else {
            // Prihlásenie na tréning
            training.participants.unshift(currentUser)
        }
    }

    return {
        scheduledTrainings,
        doneTrainings,
        scheduledCount,
        joinTraining,
        currentUser,
    }
})
