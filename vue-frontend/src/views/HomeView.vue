<script setup lang="ts">
import TrainingsAndLogout from '@/components/TrainingsAndLogout.vue'
import UserProfile from '@/components/UserProfile.vue'
import { CirclePlusFilled } from '@element-plus/icons-vue'
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import {ref} from 'vue'

const circleUrl = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')

</script>

<template>
    <div class="container">
        <CalendarAndReturnButton />
        <UserProfile :obrazok="circleUrl">
            <el-button type="primary">
                Dog<el-icon class="el-icon--right"><CirclePlusFilled /></el-icon>
            </el-button>
        </UserProfile>

        <TrainingsAndLogout />
    </div>
</template>
