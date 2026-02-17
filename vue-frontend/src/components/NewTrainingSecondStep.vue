<script setup lang="ts">
import { ref, computed } from 'vue'

const selectedTrainingTypes = ref<string[]>([])
const all = ['obedience', 'protection', 'footage']

const allChecked = computed({
    get: () => selectedTrainingTypes.value.length === all.length,
    set: (checked: boolean) => {
        selectedTrainingTypes.value = checked ? [...all] : []
    },
})

const emit = defineEmits<{
    (e: 'training-type-select', trainingTypes: string[]): void
}>()
</script>

<template>
    <div class="checkbox-buttons-container">
        <div class="checkbox-row">
            <el-checkbox-group v-model="selectedTrainingTypes" class="checkbox-group">
                <el-checkbox class="my-checkbox" label="Obedience" value="obedience" />
                <el-checkbox class="my-checkbox" label="Protection" value="protection" />
                <el-checkbox class="my-checkbox" label="Footage" value="footage" />
            </el-checkbox-group>

            <el-checkbox class="my-checkbox" v-model="allChecked" label="Every Discipline" />
        </div>

        <div style="display: flex; justify-content: space-between; width: 100%">
            <el-button>Back</el-button>
            <el-button @click="emit('training-type-select', selectedTrainingTypes)">
                Next Step
            </el-button>
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

.checkbox-row {
    display: flex;
    align-items: center;
    gap: 30px;
    flex-wrap: wrap;
}

.checkbox-group {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
}

.my-checkbox {
    color: red !important;
    --el-checkbox-checked-text-color: red !important;
    --el-checkbox-bg-color: none !important;
    --el-checkbox-checked-bg-color: red !important;
}
</style>
