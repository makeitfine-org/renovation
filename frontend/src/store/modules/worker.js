/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

import workerDataService from "@/service/WorkerDataService";

export default {
    actions: {
        retrieveWorkers({commit}) {
            workerDataService.getAll()
                .then(response => {
                    const workers = response.data
                    console.log(response.data)

                    commit('updateWorkers', workers)
                    commit('updateLoading', false)
                })
                .catch(e => {
                    console.log(e)
                })
        },
        // searchWorksByTitle({commit}, title) {
        //   workDataService.findByTitle(title)
        //       .then(response => {
        //           const works = response.data
        //           console.log(response.data)
        //
        //           commit('updateWorks', works)
        //       })
        //       .catch(e => {
        //         console.log(e);
        //       });
        // },
        // createWork({commit}, work) {
        //     workDataService.create(work)
        //         .then(response => {
        //             console.log(response.data);
        //             const newWork = response.data
        //
        //             commit('addWork', newWork)
        //             return true
        //         })
        //         .catch(e => {
        //             console.log(e);
        //             return false
        //         })
        // }
    },
    mutations: {
        updateLoading(state, loading) {
            state.loading = loading
        },
        updateWorkers(state, workers) {
            state.workers = workers
        },
        // addWork(state, newWork) {
        //     state.works.unshift(newWork)
        // }
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
