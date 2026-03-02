import { describe, expect, it } from 'vitest'
import { getDayNumber, msUntilNextCutoff, parseLocalYyyyMmDd } from '@/utils'
import { CHECK_DONE_TRAININGS_AT_HOUR, CHECK_DONE_TRAININGS_AT_MINUTE } from '@/constants'

describe('utils/index.ts', () => {
    describe('parseLocalYyyyMmDd', () => {
        it('parses local date', () => {
            const parsedDate = parseLocalYyyyMmDd('2026-02-10')
            expect(parsedDate).toEqual(new Date(2026, 1, 10))
        })
    })

    describe('msUntilNextCutoff', () => {
        it('calculates milliseconds until next cutoff', () => {
            // Use a fixed moment so the test is stable
            const now = new Date(2026, 1, 10, 12, 0, 0, 0) // Feb 10, 12:00 local

            const expectedCutoff = new Date(now)
            expectedCutoff.setHours(
                CHECK_DONE_TRAININGS_AT_HOUR,
                CHECK_DONE_TRAININGS_AT_MINUTE,
                0,
                0,
            )

            // Mirror msUntilNextCutoff rules:
            // 00:00 means "end of day" (tomorrow midnight), otherwise if already past cutoff => tomorrow
            if (CHECK_DONE_TRAININGS_AT_HOUR === 0 && CHECK_DONE_TRAININGS_AT_MINUTE === 0) {
                expectedCutoff.setDate(expectedCutoff.getDate() + 1)
            } else if (now >= expectedCutoff) {
                expectedCutoff.setDate(expectedCutoff.getDate() + 1)
            }

            const expectedMs = expectedCutoff.getTime() - now.getTime()
            const result = msUntilNextCutoff(now)

            expect(result).toBe(expectedMs)
        })
    })

    describe('getDayNumber', () => {
        it('extracts day number from date string', () => {
            const dateStr = '2026-02-10'
            const expectedDayNumber = '10'
            const result = getDayNumber(dateStr)
            expect(result).toBe(expectedDayNumber)
        })
    })
})
