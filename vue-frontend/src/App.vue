<script setup lang="ts">
import keycloak from "@/keycloak.ts";

const logout = () => {
    keycloak.logout({ redirectUri: window.location.origin });
}
</script>

<template>
    <h1>You did it!</h1>
    <p>
        Visit <a href="https://vuejs.org/" target="_blank" rel="noopener">vuejs.org</a> to read the
        documentation
    </p>
    <el-button @click="logout">Logout</el-button>
</template>
