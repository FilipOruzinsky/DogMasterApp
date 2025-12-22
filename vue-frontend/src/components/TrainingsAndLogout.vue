<script setup lang="ts">
import { List, Right } from '@element-plus/icons-vue'
import { ref } from 'vue'
import keycloak from '@/keycloak.ts'

const doneTrainings = ref(5)
const scheduledTrainings = ref(2)

const logout = () => {
    keycloak.logout({ redirectUri: window.location.origin })
}


</script>

<template>
    <div>
        <div class="trainings">
            <div class="done">Trainings done {{ doneTrainings }}</div>
            <div class="scheduled">Scheduled trainings {{ scheduledTrainings }}</div>
        </div>
        <div class="action-buttons">
            <el-button type="primary">
                Scheduled training
                <el-icon class="el-icon--right">
                    <List />
                </el-icon>
            </el-button>
            <el-button @click="logout" type="primary">
                Logout
                <el-icon class="el-icon--right">
                    <Right />
                </el-icon>
            </el-button>
        </div>
    </div>
</template>

<style scoped></style>
