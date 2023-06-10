/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.web.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import renovation.info.data.service.DetailsService
import renovation.info.generated.dgs.types.AdditionInfo
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
                id = it.id?.toHexString(),
                name = it.name,
                surname = it.surname,
                age = it.age,
                gender = it.gender,
                detailsEmails = it.detailsEmails?.asSequence()?.map {
                    DetailsEmail(it.email, it.emailStatus)
                }?.toList()
            )
        }.toList()

    @DgsData(parentType = "Details")
    @PreAuthorize("isAuthenticated() && (hasRole('SERVICE') || hasRole('ADMIN'))")
    fun additionInfos(dgsDataFetchingEnvironment: DgsDataFetchingEnvironment) =
        detailsService
            .getById(dgsDataFetchingEnvironment.getSource<Details>().id!!)
            .additionInfos?.asSequence()
            ?.map { AdditionInfo(nickName = it.nickName, phoneNumber = it.phoneNumber) }
            ?.toList()

    @DgsMutation
    @Secured(value = ["ROLE_ADMIN", "ROLE_SERVICE"])
    fun details(detailsInput: DetailsInput) = detailsService.save(detailsInput)
}
