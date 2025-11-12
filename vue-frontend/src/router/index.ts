import { createMemoryHistory, createRouter } from 'vue-router'

const router = createRouter({
    history: createMemoryHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            component: () => import('../App.vue'),
        },
    ],
})

export default router
