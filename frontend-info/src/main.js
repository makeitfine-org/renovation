/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {createApp} from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from './store'
import 'bootstrap/dist/css/bootstrap.css'

createApp(App)
    .use(router)
    .use(store)
    .mount('#app')
