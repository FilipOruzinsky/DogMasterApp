<script setup lang="ts">
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import { Check, Tools } from '@element-plus/icons-vue'
import TrainingsAndLogout from '@/components/TrainingsAndLogout.vue'
import '@/css/homeView.css'
import { reactive, ref } from 'vue'
import '@/css/editProfile.css'

const props = defineProps<{ obrazok: string }>()

const formData = reactive({
    FirstName: '',
    LastName: '',
    Address: '',
    PhoneNumber: '',
    Email: '',
    Password: '',
})



</script>

<template>
    <div class="container">
        <CalendarAndReturnButton />
        <el-icon size="50px" color="red">
            <Tools />
        </el-icon>
        <div class="avatarandform">
            <div class="avatar"><el-avatar :size="270" :src="props.obrazok" /></div>
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
        </div>

        <el-button style="max-width: fit-content; display: block; margin: 0 auto; background-color: chartreuse !important;" type="primary">
            Submit<el-icon class="el-icon--right"><Check /></el-icon>
        </el-button>

        <TrainingsAndLogout />
    </div>
</template>

<style scoped></style>
