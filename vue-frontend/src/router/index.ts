import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import EditProfileView from '@/views/EditProfileView.vue'


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView,
        },
        {
            path: '/edit-profile',
            name: 'edit-profile',
            component: EditProfileView,
        },
    ],
})

export default router
