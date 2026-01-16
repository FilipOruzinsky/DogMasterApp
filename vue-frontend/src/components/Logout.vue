<script setup lang="ts">
import { Right } from '@element-plus/icons-vue'
import keycloak from '@/keycloak.ts'
import '@/css/logout.css'

const logout = () => {
    keycloak.logout({ redirectUri: window.location.origin })
}
</script>

<template>


    <el-button class="logout" @click="logout" type="primary">
        Logout
        <el-icon class="el-icon--right">
            <Right />
        </el-icon>
    </el-button>



</template>

<style scoped></style>
