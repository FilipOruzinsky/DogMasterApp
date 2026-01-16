<script setup lang="ts">
import CalendarAndReturnButton from '@/components/CalendarAndReturnButton.vue'
import ProfileForTraining from '@/components/ProfileForTraining.vue'
import NewTrainingFirstStep from "@/components/NewTrainingFirstStep.vue";
import Logout from "@/components/Logout.vue";

const props = defineProps<{ obrazok: string }>()




</script>

<template>

    <div class="container">
        <CalendarAndReturnButton/>
        <ProfileForTraining :obrazok="props.obrazok">
            <NewTrainingFirstStep/>
        </ProfileForTraining>
        <Logout/>
    </div>
</template>

<style scoped></style>
