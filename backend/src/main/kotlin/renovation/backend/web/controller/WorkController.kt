/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web.controller

import io.micrometer.core.instrument.Counter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import renovation.backend.data.domain.Work
import renovation.backend.data.service.WorkService
import renovation.backend.data.validation.OnCreate
import renovation.backend.data.validation.OnUpdate

@CrossOrigin(originPatterns = ["http://localhost:80*", "http://r"])
@RestController
@RequestMapping("/api/work")
@Validated
class WorkController(
    @Qualifier("workServiceCacheableImpl")
    private val workService: WorkService,
    private val getAllWorksCounter: Counter,
) {
    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(WorkController::class.java)
    }

    @GetMapping
    @Operation(summary = "Get all works")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found all works",
                content = [
                    (
                        Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = (ArraySchema(schema = Schema(implementation = Work::class)))
                        )
                        )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Not authenticate", content = [Content()])]
    )
    fun list(@Parameter(description = "title-like search") @RequestParam title: String?): List<Work> {
        LOG.info("find all works")
        return if (title.isNullOrBlank()) {
            getAllWorksCounter.increment()

            workService.findAll()
        } else {
            val titleLikePattern = "%" + title.replace(" ", "%") + "%"
            workService.findByTitleLike(titleLikePattern)
        }
    }

    @GetMapping("{id}")
    fun find(@PathVariable id: UUID): Work {
        LOG.info("find work by id: $id")
        return workService.findById(id)
    }

    @PostMapping
    @Validated(OnCreate::class)
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid work: Work) {
        LOG.info("create work: $work")
        workService.save(work)
    }

    @PatchMapping("{id}")
    @Validated(OnUpdate::class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: UUID, @RequestBody @Valid work: Work) {
        LOG.info("udpate work with id = $id work: $work")
        workService.update(id, work)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        LOG.info("delete work with id = $id")
        workService.delete(id)
    }

    @GetMapping("/getAllWorksCount")
    fun getAllWorks() = getAllWorksCounter.count()
}
