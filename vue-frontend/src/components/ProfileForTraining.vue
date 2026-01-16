<script setup lang="ts">
import keycloak from '@/keycloak.ts'
import { Tools } from '@element-plus/icons-vue'
import {computed, onMounted, ref } from 'vue'
import axios from 'axios'

const props = defineProps<{ obrazok: string }>()


const dogs = ref([])

const dogNames = computed( () => dogs.value.map((dog)=>dog.name ).join())

onMounted(async () => {
    await axios
        .get(`http://localhost:8081/api/v1/dogs/owner/${keycloak.idTokenParsed?.sub}`, {
            headers: {
                Authorization: `Bearer ${keycloak.token}`,
            },
        })
        .then((response) => {
           dogs.value =response.data
        })
})
</script>

<template>
    <div class="profile">
        <div class="text-white">Handler: {{ keycloak.idTokenParsed?.given_name }}</div>
        <div class="text-white">Dog: {{dogNames}}</div>
        <el-avatar :size="170" :src="props.obrazok" />
        <slot />
    </div>
</template>

<style scoped></style>
