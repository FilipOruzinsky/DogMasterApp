<script setup lang="ts">
import { ref } from 'vue'
import {useRouter} from "vue-router";

const selectedTrainingTypes = ref<string[]>([])
const allChecked = ref(false)

const emit = defineEmits<{
    (e: 'training-type-select', trainingTypes: string[]): void
}>()




/**
 * Toggles the state of the selected training types based on the `allChecked` flag.
 * If `allChecked` is true, clears the `selectedTrainingTypes` array.
 * If `allChecked` is false, sets `selectedTrainingTypes` to a predefined list of training types.
 *
 * Dependencies:
 * - `allChecked` is an observable or reactive reference indicating whether all training types are selected.
 * - `selectedTrainingTypes` is an observable or reactive reference storing the currently selected training types.
 */
const checkAll = () => {
    if (allChecked.value) selectedTrainingTypes.value = []
    else selectedTrainingTypes.value = ['obedience', 'protection', 'footage']
}
</script>

<template>
    <div>
        <el-button>Back</el-button>
        <el-checkbox-group v-model="selectedTrainingTypes">
            <el-checkbox label="Obedience" value="obedience" />
            <el-checkbox label="Protection" value="protection" />
            <el-checkbox label="Footage" value="footage" />
        </el-checkbox-group>
        <el-checkbox @click="checkAll" label="Every Discipline" v-model="allChecked" />
        <el-button @click="emit('training-type-select', selectedTrainingTypes)">Next Step</el-button>
    </div>
</template>
