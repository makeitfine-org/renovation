/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

import workerDataService from "@/service/WorkerDataService";

export default {
    actions: {
        retrieveWorkers({commit}) {
            workerDataService.getAllData()
            // workerDataService.getWorkers()
                .then(response => {
                    const workers = response.data.data.details
                    console.log(workers)

                    commit('updateWorkers', workers)
                    commit('updateLoading', false)
                })
                .catch(e => {
                    console.log(e)
                })
        },
    },
    mutations: {
        updateLoading(state, loading) {
            state.loading = loading
        },
        updateWorkers(state, workers) {
            state.workers = workers
        },
    },
    state: {
        workers: [],
        loading: true,
    },
    getters: {
        allWorkers: (state) => state.workers,
        allWorkersCount: (state, getters) => getters.allWorkers.length,
        loading: (state) => state.loading
    }
}
