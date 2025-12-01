import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { initKeycloak } from '@/keycloak.ts'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/css/index.css'

try {
    await initKeycloak()
    const app = createApp(App)
    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }
    app.use(router)
    app.mount('#app')
} catch (error) {
    // eslint-disable-next-line no-console
    console.error('Keycloak initialization error:', error)
}
