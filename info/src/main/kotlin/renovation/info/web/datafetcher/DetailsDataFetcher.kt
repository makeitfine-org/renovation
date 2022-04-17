/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.beans.factory.annotation.Autowired
import renovation.info.data.service.DetailsService
import renovation.info.generated.dgs.types.Details
import renovation.info.generated.dgs.types.DetailsEmail
import renovation.info.generated.dgs.types.DetailsFilter
import renovation.info.generated.dgs.types.DetailsInput

@DgsComponent
class DetailsDataFetcher(
    @Autowired
    private val detailsService: DetailsService
) {
    @DgsQuery
    fun details(@InputArgument filter: DetailsFilter?) = detailsService.getAll().stream()
        .filter {
            var element = it
            filter?.let {
                filter.name?.let { if (it != element.name) return@filter false }
                filter.surname?.let { if (it != element.surname) return@filter false }
                filter.age?.let { if (it != element.age) return@filter false }
                filter.gender?.let { if (it != element.gender) return@filter false }
            }

            true
        }
        .map {
            Details(
                id = it.id,
                name = it.name,
                surname = it.surname,
                age = it.age,
                gender = it.gender,
                detailsEmails = it.detailsEmails?.asSequence()?.map {
                    DetailsEmail(it.email, it.emailStatus)
                }?.toList()
            )
        }.toList()

    @DgsMutation
    fun details(detailsInput: DetailsInput) = detailsService.save(detailsInput)
}
