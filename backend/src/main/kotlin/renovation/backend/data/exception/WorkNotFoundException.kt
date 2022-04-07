/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.exception

import java.util.UUID

class WorkNotFoundException : Exception {
    constructor(id: UUID?) : super("Work with id: $id not found")
}
