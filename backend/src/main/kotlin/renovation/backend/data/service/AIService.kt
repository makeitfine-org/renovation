/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.backend.data.service

import java.util.UUID

interface AIService {

    fun answer(id: UUID): String
}
