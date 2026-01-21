<script setup lang="ts">
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import ProfileForTraining from '@/components/ProfileForTraining.vue'
import NewTrainingFirstStep from '@/components/NewTrainingFirstStep.vue'
import Logout from '@/components/Logout.vue'
import { ref } from 'vue'
import NewTrainingSecondStep from '@/components/NewTrainingSecondStep.vue'
import Calendar from '@/components/Calendar.vue'

const props = defineProps<{ obrazok: string }>()

const step = ref(1)
const trainingGroup = ref('')
const trainingTypes = ref<string[]>([])
const calendarVisible = ref(false)

/**
 * Handles the selection of a training option by updating the training group and progressing to the next step.
 *
 * @param {string} trainingGroupEmit - The selected training option value.
 */
const handleTrainingSelect = (trainingGroupEmit: string) => {
    trainingGroup.value = trainingGroupEmit
    step.value = 2
}

/**
 * Updates the selected training types.
 *
 * This function sets the value of the `trainingTypes` state
 * with the provided array of strings representing the selected
 * training types.
 *
 * @param {string[]} selectedTrainingTypesEmit - An array of strings representing the selected training types.
 */
const handleTrainingTypesSelect = (selectedTrainingTypesEmit: string[]) => {
    trainingTypes.value = selectedTrainingTypesEmit
    calendarVisible.value = true
}



</script>

<template>
    <div class="container">
        <p style="color: white">{{ trainingGroup }}</p>
        <p style="color: white">{{ trainingTypes }}</p>
        <CalendarAndReturnButton />
        <ProfileForTraining :obrazok="props.obrazok">
            <NewTrainingFirstStep v-if="step === 1" @training-select="handleTrainingSelect" />
            <NewTrainingSecondStep
                v-else-if="step === 2"
                @training-type-select="handleTrainingTypesSelect"
            />
        </ProfileForTraining>
        <el-dialog
            v-model="calendarVisible"
            title="Warning"
            width="500"
            align-center
        >
            <Calendar />
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="calendarVisible = false">Cancel</el-button>
                    <el-button type="primary" @click="calendarVisible = false">Confirm</el-button>
                </div>
            </template>
        </el-dialog>
        <Logout />
    </div>
</template>
