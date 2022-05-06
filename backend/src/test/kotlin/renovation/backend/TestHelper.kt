/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend

import renovation.backend.data.domain.Work
import java.time.LocalDate

object TestHelper {

    @JvmStatic
    val WORKS = listOf(
        Work(
            id = "11111111-05da-40d7-9781-aad518619682",
            title = "title 1",
            description = "desc 1",
            price = 222.5,
            endDate = LocalDate.parse("2022-11-11"),
            payDate = LocalDate.parse("2022-10-25")
        ),
        Work(
            id = "22222222-05da-40d7-9781-aad518619682",
            title = "title 2",
            description = "desc 2",
            price = 5222.0,
            endDate = LocalDate.parse("2021-05-07"),
            payDate = LocalDate.parse("2022-10-15")
        ),
        Work(
            id = "33333333-05da-40d7-9781-aad518619682",
            title = "title 3",
            description = "desc 3",
            price = 222.5,
            endDate = LocalDate.parse("2022-11-11"),
        ),
    )
}
