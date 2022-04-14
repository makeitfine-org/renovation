/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.datafetcher

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.graphql.dgs.DgsQueryExecutor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.xmlunit.diff.Comparison.Detail
import renovation.info.generated.dgs.types.Details
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
internal class DetailsDataFetcherTest(
    @Autowired
    private val dgsQueryExecutor: DgsQueryExecutor
) {
    companion object {
        @JvmStatic
        private val OBJECT_MAPPER = ObjectMapper()
    }

    @Test
    fun `get details`() {
        val details: List<Detail> = dgsQueryExecutor.executeAndExtractJsonPath(
            """
            query {
                  details {
                    id
                    name
                    surname
                    age
                    gender
                  }
            }
            """,
            "data.details"
        )

        assertTrue { details.size >= 9 }
    }

    @Test
    fun `save details`() {
        val res = dgsQueryExecutor.execute(
            """
            mutation {
              details(detailsInput:{
                name:"Hello",
                gender:"M",
                surname:"There",
                age:32,
              }){
                name
                surname
              }
            }
            """
        )

        assertTrue { res.errors.isEmpty() }
        assertTrue { res.isDataPresent }

        val roughData = res.getData<LinkedHashMap<*, *>>()["details"]
        val data = OBJECT_MAPPER.convertValue(roughData, Details::class.java)
        assertEquals(Details(name = "Hello", surname = "There"), data)
    }

    @Test
    fun `filter details`() {
        val res = dgsQueryExecutor.execute(
            """
                query{
                  details(
                    filter: {
                      surname: "Hatton"
                      age: 33
                    }
                  ){
                    name
                    surname
                    age
                  }
                }
            """
        )

        assertTrue { res.errors.isEmpty() }
        assertTrue { res.isDataPresent }

        val v = res.getData<LinkedHashMap<*, *>>()["details"]
        val jsonInput: String = OBJECT_MAPPER.writeValueAsString(v)

        val result: List<Details> = OBJECT_MAPPER.readerForListOf(Details::class.java).readValue(jsonInput)
        assertEquals(2, result.size)
        assertEquals(Details(name = "Alfred", surname = "Hatton", age = 33), result[0])
        assertEquals(Details(name = "Kate", surname = "Hatton", age = 33), result[1])
    }
}
