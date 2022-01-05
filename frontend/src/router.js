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
            name: 'workFacade',
            path: '/work',
            alias: '/',
            component: () => import('@/component/WorkFacade.vue')
        },
        {
            name: 'addWork',
            path: '/add/work',
            component: () => import('@/component/AddWork.vue')
        },
    ]
}


const router = createRouter(routing)

export default router
