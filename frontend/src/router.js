/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */
import {createRouter, createWebHistory} from 'vue-router'
import Home from '@/component/Home'

const routing = {
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: Home
        },
        {
            path: '/works',
            component: () => import('@/component/Works.vue')
        },
    ]
}


const router = createRouter(routing)

export default router
