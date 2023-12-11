/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.common.web

import org.springframework.web.client.RestTemplate

object Client {
    @JvmStatic
    val RestClient = RestTemplate()
}
