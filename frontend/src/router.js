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
            name: 'home',
            path: '/home',
            component: Home
        },
        {
            name: 'work',
            path: '/work',
            alias: '/',
            component: () => import('@/component/Work.vue')
        },
    ]
}


const router = createRouter(routing)

export default router
