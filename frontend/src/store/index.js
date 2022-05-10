/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

import {createStore} from 'vuex'
import work from './modules/work'
import worker from './modules/worker'

const storing = {
    modules: {
        work,
        worker
    }
}

const store = createStore(storing)

export default store
