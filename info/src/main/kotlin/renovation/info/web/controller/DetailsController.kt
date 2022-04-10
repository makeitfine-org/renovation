/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.info.data.service.DetailsService

@RestController
@RequestMapping("/api/v1/info")
class DetailsController(
    @Autowired val detailsService: DetailsService
) {

    @GetMapping
    fun get() = detailsService.getAll()
}
