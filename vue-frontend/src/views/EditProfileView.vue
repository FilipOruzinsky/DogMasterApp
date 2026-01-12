<script setup lang="ts">
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import { Check, Tools } from '@element-plus/icons-vue'
import TrainingsAndLogout from '@/components/TrainingsAndLogout.vue'
import '@/css/homeView.css'
import { onMounted, reactive } from 'vue'
import '@/css/editProfile.css'
import keycloak from '@/keycloak.ts'
import { useStore } from 'vuex'

const store = useStore()

const props = defineProps<{ obrazok: string }>()

const formData = reactive({
    firstName: '',
    lastName: '',
    address: '',
    phoneNumber: '',
    email: '',
})

const submitForm = async () => {
    await fetch('http://localhost:8081/api/v1/users/me', {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${keycloak.token}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then((response) => response.json())
        .then((data) => {
            alert('User updated successfully')
            console.log(data)
        })
}

onMounted(async () => {
    await store.dispatch('getDogs')
    const response = await fetch('http://localhost:8081/api/v1/users/me', {
        headers: {
            Authorization: `Bearer ${keycloak.token}`,
        },
    })

    const user = await response.json()

    formData.firstName = user.firstName
    formData.lastName = user.lastName
    formData.address = user.address
    formData.phoneNumber = user.phoneNumber
    formData.email = user.email
})
</script>

<template>
    <div class="container">
        <p style="color: white">{{store.state.dogs}}</p>
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
                        <el-input v-model="formData.firstName" />
                    </el-form-item>

                    <el-form-item label="Last name">
                        <el-input v-model="formData.lastName" />
                    </el-form-item>

                    <el-form-item label="Address">
                        <el-input v-model="formData.address" />
                    </el-form-item>

                    <el-form-item label="Phone number">
                        <el-input v-model="formData.phoneNumber" />
                    </el-form-item>

                    <el-form-item label="Email">
                        <el-input v-model="formData.email" />
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
