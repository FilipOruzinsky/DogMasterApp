import { beforeEach, afterEach, describe, expect, it, vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'

// 👇 Mock constants BEFORE importing the store
vi.mock('@/constants', () => ({
    ME: 'Me',
    CHECK_DONE_TRAININGS_AT_HOUR: 0,
    CHECK_DONE_TRAININGS_AT_MINUTE: 0,
}))

import { useTrainingStore } from '@/store/trainingStore.ts'

describe('trainingStore.ts (doneTrainings with 00:00 cutoff)', () => {
    let store: ReturnType<typeof useTrainingStore>

    beforeEach(() => {
        vi.useFakeTimers()
        setActivePinia(createPinia())
        store = useTrainingStore()
    })

    afterEach(() => {
        store.$dispose()
        vi.clearAllTimers()
        vi.useRealTimers()
    })

    it('does NOT count today training before 00:00', () => {
        store.scheduledTrainings = {
            '2026-02-10': { trainingName: 'X', participants: ['Me'] },
        }

        store.__setNowForTests(new Date(2026, 1, 10, 23, 59, 59)) // Feb 10 23:59:59
        expect(store.doneTrainings).toBe(0)
    })

    it('counts today training at/after 00:00', () => {
        store.scheduledTrainings = {
            '2026-02-10': { trainingName: 'X', participants: ['Me'] },
        }

        store.__setNowForTests(new Date(2026, 1, 10, 24, 0, 0)) // Feb 10 24:00:00
        expect(store.doneTrainings).toBe(1)
    })

    it('counts past-day trainings regardless of time', () => {
        store.scheduledTrainings = {
            '2026-02-09': { trainingName: 'Past', participants: ['Me'] },
            '2026-02-10': { trainingName: 'Today', participants: ['Me'] },
        }

        store.__setNowForTests(new Date(2026, 1, 10, 10, 0, 0)) // Feb 10 10:00
        expect(store.doneTrainings).toBe(1)
    })

    it('does not count trainings where ME is not a participant', () => {
        store.scheduledTrainings = {
            '2026-02-10': { trainingName: 'X', participants: ['SomeoneElse'] },
        }

        store.__setNowForTests(new Date(2026, 1, 10, 24, 0, 0))
        expect(store.doneTrainings).toBe(0)
    })
})
