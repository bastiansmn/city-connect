import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import { createPinia } from 'pinia'

const pinia = createPinia()


// Vant component lib
import vant from 'vant'
import 'vant/lib/index.css';

import "leaflet/dist/leaflet.css";

createApp(App)
   .use(pinia)
   .use(router)
   .use(vant)
   .mount('#app')
