/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import renovation.backend.data.domain.Worker
import renovation.backend.data.service.GraphQlService

@Service
class GraphQlServiceImpl(
    @Value("\${info-service.graphql-url}")
    private val infoServiceBaseUrl: String,
    private val restTemplate: RestTemplate,
) : GraphQlService {
    companion object {
        @JvmStatic
        private val OBJECT_MAPPER = ObjectMapper()
    }

    override fun getDetails(graphQlBody: String): List<Worker> {
        val detailsRow = requestData(graphQlBody)["details"]
        return list(detailsRow)
    }

    private inline fun <reified T> list(rowList: Any?): List<T> {
        val jsonInput: String = OBJECT_MAPPER.writeValueAsString(rowList)
        return OBJECT_MAPPER.readerForListOf(T::class.java).readValue(jsonInput)
    }

    private fun requestData(graphQlBody: String): Map<*, *> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/json")

        return (
            restTemplate.postForEntity(
                infoServiceBaseUrl,
                HttpEntity(
                    "{\"query\": \"${graphQlBody.replace("\n", "\\n")}\"}",
                    httpHeaders
                ),
                Any::class.java
            ).body as Map<*, *>
            )["data"] as Map<*, *>
    }
}
