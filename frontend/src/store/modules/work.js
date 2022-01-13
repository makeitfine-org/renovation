/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

import workDataService from "@/service/WorkDataService";

export default {
    actions: {
        retrieveWorks({commit}) {
            workDataService.getAll()
                .then(response => {
                    const works = response.data
                    console.log(response.data)

                    commit('updateWorks', works)
                    commit('updateLoading', false)
                })
                .catch(e => {
                    console.log(e)
                })
        },
        searchWorksByTitle({commit}, title) {
          workDataService.findByTitle(title)
              .then(response => {
                  const works = response.data
                  console.log(response.data)

                  commit('updateWorks', works)
              })
              .catch(e => {
                console.log(e);
              });
        }
    },
    mutations: {
        updateWorks(state, works) {
            state.works = works
        },
        updateLoading(state, loading) {
            state.loading = loading
        }
    },
    state: {
        works: [],
        loading: true,
    },
    getters: {
        allWorks: (state) => state.works,
        allWorksCount: (state, getters) => getters.allWorks.length,
        loading: (state) => state.loading
    }
}
