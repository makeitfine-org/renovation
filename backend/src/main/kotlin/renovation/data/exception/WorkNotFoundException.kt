/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.data.exception

class WorkNotFoundException : Exception {
    constructor(id: Long?) : super("Work with id: ${id} not found")
}
