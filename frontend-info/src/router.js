/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */
import {createRouter, createWebHistory} from 'vue-router'
import Home from '@/component/Home'

const routing = {
    history: createWebHistory(),
    routes: [
        {
            name: 'workerFacade',
            path: '/worker',
            alias: '/',
            component: () => import('@/component/worker/WorkerFacade.vue')
        },
    ]
}


const router = createRouter(routing)

export default router
