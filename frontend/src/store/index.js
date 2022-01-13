/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

import {createStore} from 'vuex'
import work from './modules/work'

const storing = {
    modules: {
        work
    }
}

const store = createStore(storing)

export default store
