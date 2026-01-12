import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import keycloak, { initKeycloak } from '@/keycloak.ts'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/css/index.css'
import { createStore } from 'vuex'
import axios from 'axios'

try {
    await initKeycloak()
    const store = createStore({
        state() {
            return {
                dogs: [],
            }
        },
        mutations: {
            setDogs(state, payload) {
                state.dogs = payload
            },
        },
        actions: {
            async getDogs({ commit }) {
                await axios
                    .get(`http://localhost:8081/api/v1/dogs/owner/${keycloak.idTokenParsed?.sub}`, {
                        headers: {
                            Authorization: `Bearer ${keycloak.token}`,
                        },
                    })
                    .then((response) => {
                        commit('setDogs', response.data)
                    })
            },
        },
    })
    const app = createApp(App)
    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }
    app.use(router)
    app.use(store)
    app.mount('#app')
} catch (error) {
    // eslint-disable-next-line no-console
    console.error('Keycloak initialization error:', error)
}
