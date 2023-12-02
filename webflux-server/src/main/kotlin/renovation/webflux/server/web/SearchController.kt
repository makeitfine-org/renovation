package renovation.webflux.server.web

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import renovation.webflux.server.dto.IdNameTypeResponse
import renovation.webflux.server.dto.ResultType
import renovation.webflux.server.model.Company
import renovation.webflux.server.model.User
import renovation.webflux.server.service.CompanyService
import renovation.webflux.server.service.UserService

@RestController
@RequestMapping("/api/search")
class SearchController(
    private val userService: UserService,
    private val companyService: CompanyService
) {

    @GetMapping
    suspend fun searchByNames(
        @RequestParam(name = "query") query: String
    ): Flow<IdNameTypeResponse> {
        val usersFlow = userService.findAllUsersByNameLike(name = query)
            .map(User::toIdNameTypeResponse)
        val companiesFlow = companyService.findAllCompaniesByNameLike(name = query)
            .map(Company::toIdNameTypeResponse)

        return merge(usersFlow, companiesFlow)
    }
}

private fun User.toIdNameTypeResponse(): IdNameTypeResponse =
    IdNameTypeResponse(
        id = this.id!!,
        name = this.name,
        type = ResultType.USER
    )

private fun Company.toIdNameTypeResponse(): IdNameTypeResponse =
    IdNameTypeResponse(
        id = this.id!!,
        name = this.name,
        type = ResultType.COMPANY
    )
