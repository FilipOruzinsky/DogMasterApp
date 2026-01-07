<script setup lang="ts">
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import { Check, Tools } from '@element-plus/icons-vue'
import TrainingsAndLogout from '@/components/TrainingsAndLogout.vue'
import '@/css/homeView.css'
import { onMounted, reactive, ref } from 'vue'
import '@/css/editProfile.css'
import keycloak from '@/keycloak.ts'

const props = defineProps<{ obrazok: string }>()

const formData = reactive({
    FirstName: '',
    LastName: '',
    Address: '',
    PhoneNumber: '',
    Email: '',
    Password: '',
})

onMounted(async () => {
    const response = await fetch('http://localhost:8081/api/v1/users/me', { // toto vrati HTML
        headers: {

            Authorization: `Bearer ${keycloak.token}`,
        },
    })

    const user = await response.json()


    formData.FirstName = user.firstName
    formData.LastName = user.lastName
    formData.Address = user.address
    formData.PhoneNumber = user.phoneNumber
    formData.Email = user.email
})
// onMounted(async () => {
//     console.log('EDIT PROFILE onMounted START')
//
//     try {
//         console.log('Before fetch')
//         const response = await fetch('http://localhost:8081/api/v1/users/me')
//         console.log('After fetch, status:', response.status)
//
//         const text = await response.text()
//         console.log('Raw response text:', text)
//     } catch (e) {
//         console.log('FETCH FAILED', e)
//     }
// })
</script>

<template>
    <div class="container">
        <CalendarAndReturnButton />
        <el-icon size="50px" color="red">
            <Tools />
        </el-icon>
        <div class="avatarandform">
            <div class="avatar"><el-avatar :size="270" :src="props.obrazok" /></div>
            <div>
                <el-form :model="formData" label-position="left" label-width="auto">
                    <el-form-item label="Name">
                        <el-input v-model="formData.FirstName" />
                    </el-form-item>

                    <el-form-item label="Last name">
                        <el-input v-model="formData.LastName" />
                    </el-form-item>

                    <el-form-item label="Address">
                        <el-input v-model="formData.Address" />
                    </el-form-item>

                    <el-form-item label="Phone number">
                        <el-input v-model="formData.PhoneNumber" />
                    </el-form-item>

                    <el-form-item label="Email">
                        <el-input v-model="formData.Email" />
                    </el-form-item>

                    <el-form-item label="Password">
                        <el-input v-model="formData.Password" />
                    </el-form-item>
                </el-form>

                <el-button class="submit-button" type="primary">
                    Submit<el-icon class="el-icon--right"><Check /></el-icon>
                </el-button>
            </div>
        </div>

        <TrainingsAndLogout />
    </div>
</template>

<style scoped></style>
