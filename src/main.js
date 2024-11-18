import { createApp } from 'vue'
import axios from 'axios'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from "@/router";
import App from './App.vue'
import store from "@/store";
import Cookies from 'js-cookie';

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
axios.defaults.baseURL = 'http://localhost:8082';
app.use(ElementPlus)
app.use(router)
app.use(store)
app.use(Cookies)
app.mount('#app')