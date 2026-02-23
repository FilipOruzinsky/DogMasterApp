<script setup lang="ts">
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import ProfileForTraining from '@/components/ProfileForTraining.vue'
import NewTrainingFirstStep from '@/components/NewTrainingFirstStep.vue'
import Logout from '@/components/Logout.vue'
import { ref } from 'vue'
import NewTrainingSecondStep from '@/components/NewTrainingSecondStep.vue'
import Calendar from '@/components/Calendar.vue'
import axios from 'axios'
import keycloak from '@/keycloak.ts'
import { useRouter } from 'vue-router'

const props = defineProps<{ obrazok: string }>()

const step = ref(1)
const trainingGroup = ref('')
const trainingTypes = ref<string[]>([])
const calendarVisible = ref(false)
const trainingDate = ref('')
const router = useRouter()

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

const handleSubmit = async () => {
    calendarVisible.value = false
    console.log(trainingDate.value)
    axios.post(
        'http://localhost:8081/api/v1/trainings',
        {
            trainingsNames: trainingTypes.value.join(','),
            trainingtype: trainingGroup.value.toUpperCase(),
            trainingDate: trainingDate.value,
        },
        {
            headers: {
                Authorization: `Bearer ${keycloak.token}`,
            },
        },
    )
    router.push('/')
}

const handleDateSelection = (date: string) => {
    trainingDate.value = date
    console.log(trainingDate.value)
}
</script>

<template>
    <div class="container">
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
            title="Training Calendar"
            width="90vw"
            class="large-calendar-dialog"
            append-to-body
            align-center
            :show-close="false"
        >
            <Calendar :training-type="trainingTypes.join(',')" @date-selected="handleDateSelection" />
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="calendarVisible = false">Cancel</el-button>
                    <el-button type="primary" @click="handleSubmit()">Confirm</el-button>
                </div>
            </template>
        </el-dialog>
        <Logout />
    </div>
</template>

<style scoped>
/* Use :deep because el-dialog is appended to body and might sit outside the scoped scope */
:deep(.large-calendar-dialog) {
    height: 90vh;
    display: flex;
    flex-direction: column;
    margin-bottom: 0; /* Override default margins to keep it centered */
}

:deep(.large-calendar-dialog .el-dialog__body) {
    flex: 1;
    overflow-y: auto;
}
</style>
