import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { initKeycloak } from '@/keycloak.ts'

try {
  await initKeycloak()
  const app = createApp(App)
  app.use(router)
  app.mount('#app')
} catch (error) {
  // eslint-disable-next-line no-console
  console.error('Keycloak initialization error:', error)
}
