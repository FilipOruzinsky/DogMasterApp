<script setup lang="ts">
import { Tools } from '@element-plus/icons-vue'
import { ref } from 'vue'


const circleUrl = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')

</script>

<template>
    <div class="profile">
        <div class="handler">Handler: John Doe</div>
        <el-icon size="50px" color="red">
            <Tools />
        </el-icon>

        <el-avatar :size="170" :src="circleUrl" />
        <slot />


    </div>
</template>

<style scoped>

</style>
