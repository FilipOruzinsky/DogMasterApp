<script setup lang="ts">
import TrainingsAndLogout from '@/components/TrainingsAndLogout.vue'
import UserProfile from '@/components/UserProfile.vue'
import { CirclePlusFilled } from '@element-plus/icons-vue'
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
</script>

<template>
    <div class="container">
        <CalendarAndReturnButton />
        <UserProfile>
            <el-button type="primary">
                Dog<el-icon class="el-icon--right"><CirclePlusFilled /></el-icon>
            </el-button>
        </UserProfile>

        <TrainingsAndLogout />
    </div>
</template>
