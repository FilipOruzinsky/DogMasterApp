import { CHECK_DONE_TRAININGS_AT_HOUR, CHECK_DONE_TRAININGS_AT_MINUTE } from '@/constants'

/**
 * Parses a date string in the format 'YYYY-MM-DD' and returns a Date object
 * representing the local time at midnight for the given date.
 *
 * @example parseLocalYyyyMmDd('2023-01-01') // returns a Date object representing January 1, 2023, at midnight
 *
 * @param {string} dateStr - The date string to parse, formatted as 'YYYY-MM-DD'.
 * @returns {Date} A Date object representing the parsed local date at midnight.
 */
export const parseLocalYyyyMmDd = (dateStr: string): Date => {
    const [y, m, d] = dateStr.split('-').map(Number)
    return new Date(y, m - 1, d)
}

/**
 * Calculates the number of milliseconds remaining until the next daily cutoff time.
 *
 * The daily cutoff time is determined by predefined constants for the hour and minute.
 * It calculates the cutoff time for the next day based on the provided `Date` object.
 *
 * @param {Date} from - The reference date and time from which the calculation should start.
 * @returns {number} The number of milliseconds until the next daily cutoff time.
 */
export const msUntilNextCutoff = (from: Date): number => {
    const next = new Date(from)
    next.setHours(CHECK_DONE_TRAININGS_AT_HOUR, CHECK_DONE_TRAININGS_AT_MINUTE, 0, 0)

    next.setDate(next.getDate() + 1)

    return next.getTime() - from.getTime()
}

/**
 * Extracts and returns the day number from a date string formatted as "YYYY-MM-DD".
 *
 * @param {string} dateStr - The input date string in the format "YYYY-MM-DD".
 * @returns {string} The day number extracted from the date string.
 */
export const getDayNumber = (dateStr: string): string => {
    return dateStr.split('-').slice(2).join('')
}
