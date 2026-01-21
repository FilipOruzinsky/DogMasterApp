<script setup lang="ts">


</script>

<template>
    <el-calendar>
        <template #date-cell="{ data }">
            <p :class="data.isSelected ? 'is-selected' : ''">
                {{ data.day.split('-').slice(1).join('-') }}
                {{ data.isSelected ? '✔️' : '' }}
            </p>
        </template>
    </el-calendar>
</template>

<style scoped></style>
