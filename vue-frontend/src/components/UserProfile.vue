<script setup lang="ts">
import { Tools } from '@element-plus/icons-vue'

import { useRouter } from 'vue-router'
import keycloak from "@/keycloak.ts";

const props = defineProps<{ obrazok: string }>()

const router = useRouter()

const handleToolsClick = () => {
    router.push('edit-profile')
}


</script>

<template>
    <div class="profile">
        <div class="handler">Handler: {{keycloak.idTokenParsed?.given_name}}</div>

        <el-icon
            size="50px"
            color="red"
            @click="handleToolsClick"
            style="cursor: pointer;">
            <Tools />
        </el-icon>

        <el-avatar :size="170" :src="props.obrazok" />
        <slot />
    </div>
</template>

<style scoped></style>
