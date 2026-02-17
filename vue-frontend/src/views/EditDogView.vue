<script setup lang="ts">
import TrainingsAndLogout from '@/components/TrainingsAndLogout.vue'
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import { Check, Tools } from '@element-plus/icons-vue'
import { reactive } from 'vue'
import keycloak from '@/keycloak.ts'

const props = defineProps<{ obrazok: string }>()

const formData = reactive({
    name: '',
    breed: '',
    sex: '',
    age: '',
})

const submitForm = async () => {
    await fetch('http://localhost:8081/api/v1/dogs', {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${keycloak.token}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then((response) => response.json())
        .then(() => {
            alert('Dog created successfully')
        })
}
</script>

<template>
    <div class="container">
        <CalendarAndReturnButton />
        <el-icon size="50px" color="red">
            <Tools />
        </el-icon>
        <div class="avatarandform">
            <div class="avatar">
                <el-avatar :size="270" :src="props.obrazok" />
            </div>
            <div>
                <el-form :model="formData" label-position="left" label-width="auto">
                    <el-form-item label="Name">
                        <el-input v-model="formData.name" />
                    </el-form-item>

                    <el-form-item label="Breed">
                        <el-input v-model="formData.breed" />
                    </el-form-item>

                    <el-form-item label="Sex">
                        <el-input v-model="formData.sex" />
                    </el-form-item>

                    <el-form-item label="Age">
                        <el-input type="number" v-model="formData.age" />
                    </el-form-item>
                </el-form>

                <el-button class="submit-button" type="primary" @click="submitForm">
                    Submit
                    <el-icon class="el-icon--right">
                        <Check />
                    </el-icon>
                </el-button>
            </div>
        </div>

        <TrainingsAndLogout />
    </div>
</template>

<style scoped></style>
