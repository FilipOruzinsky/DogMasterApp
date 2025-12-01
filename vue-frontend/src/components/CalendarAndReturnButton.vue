<script setup lang="ts">
const date = new Date()

</script>

<template>
    <div class="current-day">{{date.getDate()}}. {{date.getMonth()}}. {{date.getFullYear()}}</div>
</template>

<style scoped></style>
