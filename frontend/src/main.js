/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

import {createApp} from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from './store'

createApp(App)
    .use(router)
    .use(store)
    .mount('#app')
