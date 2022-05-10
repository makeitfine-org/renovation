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
            name: "work-details",
            path: "/work/:id",
            component: () => import("@/component/Work")
        },
        {
            name: 'addWork',
            path: '/add/work',
            component: () => import('@/component/AddWork.vue')
        },
        {
            name: 'workerFacade',
            path: '/worker',
            component: () => import('@/component/worker/WorkerFacade.vue')
        },
    ]
}


const router = createRouter(routing)

export default router
