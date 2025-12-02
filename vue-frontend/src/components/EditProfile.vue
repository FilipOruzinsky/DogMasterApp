<script setup lang="ts">
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import { Tools } from '@element-plus/icons-vue'

const props = defineProps<{ obrazok: string }>()
</script>

<template>
    <CalendarAndReturnButton />
    <div>
        <el-icon size="50px" color="red">
            <Tools />
        </el-icon>
    </div>
    <div><el-avatar :size="170" :src="props.obrazok" /></div>
</template>

<style scoped></style>
