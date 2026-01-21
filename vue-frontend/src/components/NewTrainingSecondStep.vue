<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const selectedTrainingTypes = ref<string[]>([])
const allChecked = ref(false)

const emit = defineEmits<{
    (e: 'training-type-select', trainingTypes: string[]): void
}>()

/**
 * Toggles the state of the selected training types based on the `allChecked` flag.
 * If `allChecked` is true, it clears the `selectedTrainingTypes` array.
 * If `allChecked` is false, sets `selectedTrainingTypes` to a predefined list of training types.
 *
 * Dependencies:
 * - `allChecked` is an observable or reactive reference indicating whether all training types are selected.
 * - `selectedTrainingTypes` is an observable or reactive reference storing the currently selected training types.
 */
const checkAll = () => {
    allChecked.value = !allChecked.value
    if (allChecked.value) {
        selectedTrainingTypes.value = []
        // allChecked.value = false
    } else {
        selectedTrainingTypes.value = ['obedience', 'protection', 'footage']
        // allChecked.value = true
    }
}

watch(selectedTrainingTypes, () => (allChecked.value = selectedTrainingTypes.value.length === 3))

watch(allChecked, () => (selectedTrainingTypes.value = allChecked.value ? ['obedience', 'protection', 'footage'] : []))
</script>

<template>
    <div>
        <div class="checkbox-buttons-container">
            <el-checkbox-group v-model="selectedTrainingTypes">
                <el-checkbox class="my-checkbox" label="Obedience" value="obedience" />
                <el-checkbox class="my-checkbox" label="Protection" value="protection" />
                <el-checkbox class="my-checkbox" label="Footage" value="footage" />
                <el-checkbox
                    class="my-checkbox"
                    @click="checkAll"
                    label="Every Discipline"
                    :value="allChecked"
                />
            </el-checkbox-group>

            <div style="display: flex; justify-content: space-between; width: 100%">
                <el-button>Back</el-button>
                <el-button @click="emit('training-type-select', selectedTrainingTypes)"
                >Next Step
                </el-button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.checkbox-buttons-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
}

.checkbox-container {
    display: flex;
    align-items: center;
    gap: 16px;
    justify-content: space-between;
}

.my-checkbox {
    color: red !important;
    --el-checkbox-checked-text-color: red !important;
    --el-checkbox-bg-color: none !important;
    --el-checkbox-checked-bg-color: red !important;
}
</style>
