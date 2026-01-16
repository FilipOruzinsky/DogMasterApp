<script setup lang="ts">
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import ProfileForTraining from '@/components/ProfileForTraining.vue'
import NewTrainingFirstStep from '@/components/NewTrainingFirstStep.vue'
import Logout from '@/components/Logout.vue'
import { ref } from 'vue'
import NewTrainingSecondStep from '@/components/NewTrainingSecondStep.vue'

const props = defineProps<{ obrazok: string }>()

const step = ref(1)
const trainingGroup = ref('')
const trainingTypes = ref<string[]>([])

/**
 * Handles the selection of a training option by updating the training group and progressing to the next step.
 *
 * @param {string} v - The selected training option value.
 */
const handleTrainingSelect = (v: string) => {
    trainingGroup.value = v
    step.value = 2
}

/**
 * Updates the selected training types.
 *
 * This function sets the value of the `trainingTypes` state
 * with the provided array of strings representing the selected
 * training types.
 *
 * @param {string[]} v - An array of strings representing the selected training types.
 */
const handleTrainingTypesSelect = (v: string[]) => {
    trainingTypes.value = v
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
        <Logout />
    </div>
</template>
