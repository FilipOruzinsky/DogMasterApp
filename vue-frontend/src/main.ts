import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { initKeycloak } from '@/keycloak.ts'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/css/index.css'
// import 'element-plus/dist/index.css'
import { createPinia } from 'pinia'

try {
    await initKeycloak()
    const app = createApp(App)
    const pinia = createPinia()

    app.use(pinia)

    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }
    app.use(router)
    app.mount('#app')
} catch (error) {
    // eslint-disable-next-line no-console
    console.error('Keycloak initialization error:', error)
}
