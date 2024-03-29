/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.info.data.service.DetailsService

@CrossOrigin(originPatterns = ["http://localhost:80*", "http://r"])
@RestController
@RequestMapping("/api/v1/info")
class DetailsController(
    @Autowired val detailsService: DetailsService
) {
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun get() = detailsService.getAll()
}
