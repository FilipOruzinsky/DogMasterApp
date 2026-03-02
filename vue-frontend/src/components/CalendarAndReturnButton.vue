<script setup lang="ts">
import { Back } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import Calendar from '@/components/Calendar.vue'
import { ref } from 'vue'

const date = new Date()

const router = useRouter()

const calendarVisible = ref(false)

const handleBackClick = () => {
    router.push('/')
}

const handleDateClick = () => {
    calendarVisible.value = true
}
</script>

<template>
    <div class="calendarAndReturn">
        <el-button type="primary" @click="handleDateClick">
            <div class="current-day">
                {{ date.getDate() }}. {{ date.getMonth() }}. {{ date.getFullYear() }}
            </div>
        </el-button>

        <el-dialog
            v-model="calendarVisible"
            title="Training Calendar"
            width="90vw"
            class="large-calendar-dialog"
            append-to-body
            align-center
            :show-close="false"
        >
            <Calendar />
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="calendarVisible = false">Cancel</el-button>
                </div>
            </template>
        </el-dialog>

        <el-button type="primary">
            <el-icon @click="handleBackClick">
                <Back />
            </el-icon>
        </el-button>
    </div>
</template>

<style scoped></style>
