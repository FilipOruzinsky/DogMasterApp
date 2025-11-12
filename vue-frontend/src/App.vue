<script setup lang="ts">
import keycloak from '@/keycloak.ts'
import { onMounted, ref } from 'vue'
import axios from 'axios'
import type {User} from "@/types/User.ts";

const userData = ref<User | null>(null)
const loading = ref(true)

const logout = () => {
    keycloak.logout({ redirectUri: window.location.origin })
}

onMounted(async () => {
    try {

        if (!keycloak.authenticated) {
            console.log('Not authenticated, waiting for login...')
            loading.value = false
            return
        }


        await keycloak.updateToken(30)

        console.log('Making request with token:', keycloak.token?.substring(0, 20) + '...')

        const response = await axios.get('http://localhost:8081/api/v1/users/me', {
            headers: {
                'Authorization': `Bearer ${keycloak.token}`
            }
        })

        userData.value = response.data
        console.log('User data:', userData.value)
    } catch (error) {
        console.error('Error fetching user:', error)
    } finally {
        loading.value = false
    }
})
</script>

<template>
    <div v-if="loading">Loading user data...</div>
    <div v-else-if="!keycloak.authenticated">
        <p>Redirecting to login...</p>
    </div>
    <div v-else>
        <h1>You did it!</h1>
        <p v-if="userData">Welcome, {{ userData.firstName }}!</p>

        <el-button @click="logout">Logout</el-button>
    </div>
</template>
