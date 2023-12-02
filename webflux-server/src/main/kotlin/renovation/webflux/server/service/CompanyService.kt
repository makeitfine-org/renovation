package renovation.webflux.server.service

import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import renovation.webflux.server.model.Company
import renovation.webflux.server.repository.CompanyRepository

@Service
class CompanyService(
    private val companyRepository: CompanyRepository
) {

    suspend fun saveCompany(company: Company): Company? =
        companyRepository.save(company)

    suspend fun findAllCompanies(): Flow<Company> =
        companyRepository.findAll()

    suspend fun findCompanyById(id: Long): Company? =
        companyRepository.findById(id)

    suspend fun deleteCompanyById(id: Long) {
        val foundCompany = companyRepository.findById(id)

        foundCompany?.apply { companyRepository.deleteById(id) } ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Company with id $id was not found."
        )
    }

    suspend fun findAllCompaniesByNameLike(name: String): Flow<Company> =
        companyRepository.findByNameContaining(name)

    @Suppress("detekt:UnusedPrivateMember")
    suspend fun updateCompany(id: Long, requestedCompany: Company): Company {
        val foundCompany = companyRepository.findById(id)

        return if (foundCompany == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Company with id $id was not found.")
        } else {
            companyRepository.save(
                foundCompany.copy(id = foundCompany.id)
            )
        }
    }
}
