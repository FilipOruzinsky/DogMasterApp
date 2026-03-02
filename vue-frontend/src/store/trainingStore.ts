import { defineStore } from 'pinia'
import { ref, computed, onScopeDispose } from 'vue'
import { CHECK_DONE_TRAININGS_AT_HOUR, CHECK_DONE_TRAININGS_AT_MINUTE, ME } from '@/constants'
import type { TrainingDay } from '@/interfaces/TrainingDay.ts'
import { msUntilNextCutoff, parseLocalYyyyMmDd } from '@/utils'

export const useTrainingStore = defineStore('training', () => {
    let timeoutId: number | undefined

    //#region ref
    const now = ref(new Date())
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
    //#endregion

    //#region computed
    const doneTrainings = computed(() => {
        const current = now.value
        const todayStart = _startOfDay(current)
        const cutoff = _cutoffForToday(todayStart)

        return Object.entries(scheduledTrainings.value).filter(([date, training]) => {
            const trainingDay = parseLocalYyyyMmDd(date)
            return (
                _isDoneTrainingDay(trainingDay, todayStart, current, cutoff) &&
                isMeInTraining(training)
            )
        }).length
    })

    const scheduledCount = computed(() => {
        const todayStart = _startOfDay(new Date())

        return Object.entries(scheduledTrainings.value).filter(([date, training]) => {
            const trainingDay = parseLocalYyyyMmDd(date)
            return _isScheduledTrainingDay(trainingDay, todayStart) && isMeInTraining(training)
        }).length
    })
    //#endregion

    const scheduleNextTick = () => {
        if (timeoutId) window.clearTimeout(timeoutId)

        const delay = msUntilNextCutoff(new Date())
        timeoutId = window.setTimeout(() => {
            now.value = new Date()
            scheduleNextTick()
        }, delay)
    }

    scheduleNextTick()

    onScopeDispose(() => {
        if (timeoutId) window.clearTimeout(timeoutId)
    })

    const _startOfDay = (date: Date): Date => {
        return new Date(date.getFullYear(), date.getMonth(), date.getDate())
    }

    const _cutoffForToday = (todayStart: Date): Date => {
        const cutoff = new Date(todayStart)
        cutoff.setHours(CHECK_DONE_TRAININGS_AT_HOUR, CHECK_DONE_TRAININGS_AT_MINUTE, 0, 0)

        // Same meaning: 00:00 cutoff means "end of day" => tomorrow 00:00
        if (CHECK_DONE_TRAININGS_AT_HOUR === 0 && CHECK_DONE_TRAININGS_AT_MINUTE === 0) {
            cutoff.setDate(cutoff.getDate() + 1)
        }

        return cutoff
    }

    const _isDoneTrainingDay = (
        trainingDay: Date,
        todayStart: Date,
        current: Date,
        cutoff: Date,
    ): boolean => {
        const isBeforeToday = trainingDay < todayStart
        const isToday = trainingDay.getTime() === todayStart.getTime()
        const isDoneTodayAfterCutoff = isToday && current >= cutoff
        return isBeforeToday || isDoneTodayAfterCutoff
    }

    const _isScheduledTrainingDay = (trainingDay: Date, todayStart: Date): boolean => {
        return trainingDay >= todayStart
    }

    const _ensureTrainingDay = (day: string, trainingType: string): TrainingDay => {
        if (!scheduledTrainings.value[day]) {
            scheduledTrainings.value[day] = {
                trainingName: trainingType.toUpperCase(),
                participants: [],
            }
        }
        return scheduledTrainings.value[day]
    }

    const _isParticipant = (training: TrainingDay, participant: string): boolean => {
        return training.participants.includes(participant)
    }

    const _addParticipantToFront = (training: TrainingDay, participant: string): void => {
        training.participants.unshift(participant)
    }

    const _removeParticipant = (training: TrainingDay, participant: string): void => {
        const index = training.participants.indexOf(participant)
        if (index > -1) training.participants.splice(index, 1)
    }

    const _deleteDayIfNoParticipants = (day: string): void => {
        const training = scheduledTrainings.value[day]
        if (training && training.participants.length === 0) {
            delete scheduledTrainings.value[day]
        }
    }

    const isMeInTraining = (training: TrainingDay): boolean => {
        return training.participants.includes(ME)
    }

    const joinTraining = (day: string, trainingType: string): void => {
        const training = _ensureTrainingDay(day, trainingType)

        if (_isParticipant(training, ME)) {
            // Odhlásenie z tréningu
            _removeParticipant(training, ME)
            _deleteDayIfNoParticipants(day)
        } else {
            // Prihlásenie na tréning
            _addParticipantToFront(training, ME)
        }
    }

    const hasParticipants = (date: string): boolean => {
        return scheduledTrainings.value[date]?.participants.length > 0
    }

    //#region testing helpers
    const __setNowForTests = (date: Date) => {
        now.value = date
    }
    //#endregion

    return {
        scheduledTrainings,
        doneTrainings,
        scheduledCount,
        isMeInTraining,
        joinTraining,
        hasParticipants,
        // testing helpers
        __setNowForTests,
    }
})
