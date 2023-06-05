/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.common.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Date {

    @JvmStatic
    private val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss SSS")

    @JvmStatic
    fun formattedNow() = LocalDateTime.now().format(DATE_TIME_FORMAT)!!

    @JvmStatic
    fun formattedDate(dateTime: LocalDateTime) = dateTime.format(DATE_TIME_FORMAT)!!

    @JvmStatic
    fun formattedDate(dateTime: LocalDateTime, dateTimePattern: String) =
        dateTime.format(DateTimeFormatter.ofPattern(dateTimePattern))!!
}
