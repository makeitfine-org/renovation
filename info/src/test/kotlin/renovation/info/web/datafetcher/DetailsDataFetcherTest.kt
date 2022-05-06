/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.datafetcher

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.graphql.dgs.DgsQueryExecutor
import graphql.ErrorType
import org.bson.types.ObjectId
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.xmlunit.diff.Comparison.Detail
import renovation.info.data.repository.DetailsRepository
import renovation.info.generated.dgs.types.Details
import renovation.info.generated.dgs.types.DetailsEmail
import renovation.info.generated.dgs.types.EmailStatus
import renovation.info.generated.dgs.types.Gender
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Tag("smoke")
@SpringBootTest
internal class DetailsDataFetcherTest(
    @Autowired
    private val dgsQueryExecutor: DgsQueryExecutor,
    @Autowired
    private val detailsRepository: DetailsRepository
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
                name:"Hello"
                gender:Male
                surname:"There"
                age:32
                detailsEmails: {
                  email:"temo@eet.ddd"
                  emailStatus: Active
                }
              }){
                id
                name
                surname
                gender
                age
                detailsEmails {
                    email
                    emailStatus
                }
              }
            }
            """
        )

        assertTrue { res.errors.isEmpty() }
        assertTrue { res.isDataPresent }

        val roughData = res.getData<LinkedHashMap<*, *>>()["details"]
        val data = OBJECT_MAPPER.convertValue(roughData, Details::class.java)
        assertEquals(
            Details(
                id = data.id, name = "Hello", surname = "There", age = 32, gender = Gender.Male,
                detailsEmails = listOf(DetailsEmail("temo@eet.ddd", EmailStatus.Active))
            ),
            data
        )

        // Clean db after test
        detailsRepository.deleteById(ObjectId(data.id))
    }

    @Test
    fun `save details (failed) invalid input data`() {
        val res = dgsQueryExecutor.execute(
            """
            mutation {
              details(detailsInput:{
                name:"Hello"
                gender:Male
                surname:"There"
                age:"32a"
              }){
                id
                name
              }
            }
            """
        )

        assertTrue { res.errors.isNotEmpty() }
        assertEquals(1, res.errors.size)
        assertEquals(ErrorType.ValidationError, res.errors[0].errorType)

        assertEquals(
            "Validation error of type WrongType: argument 'detailsInput.age' " +
                "with value 'StringValue{value='32a'}' is not a valid 'Age' - " +
                "Expected AST type 'IntValue' but was 'StringValue'. @ 'details'",
            res.errors[0].message
        )
        assertFalse { res.isDataPresent }
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
                    detailsEmails {
                      email
                      emailStatus
                    }
                    additionInfos {
                      nickName
                      phoneNumber
                    }
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
        assertEquals(
            Details(
                name = "Kate", surname = "Hatton", age = 33,
                detailsEmails = listOf(
                    DetailsEmail("kh33@email.com", EmailStatus.Active),
                    DetailsEmail("kh33_other@email.two", EmailStatus.Inactive)
                )
            ),
            result[1]
        )
    }

    @Test
    fun `save details (invalid input data)`() {
        val res = dgsQueryExecutor.execute(
            """
            mutation {
              details(detailsInput:{
                name:"Hello"
                gender:Male
                surname:"There"
                age:321
                detailsEmails: {
                  email:"temo@eet.ddd"
                  emailStatus: Active
                }
              }){
                id
                name
                surname
                gender
                age
                detailsEmails {
                    email
                    emailStatus
                }
              }
            }
            """
        )

        assertTrue { res.errors.isNotEmpty() }
        assertEquals(1, res.errors.size)

        assertEquals(
            "javax.validation.ConstraintViolationException: validate.validatedEntity.age: Age should be up to 90",
            res.errors[0].message
        )

        assertTrue { res.isDataPresent }
        assertTrue { res.getData<LinkedHashMap<*, *>>().containsKey("details") }
        assertNull(res.getData<LinkedHashMap<*, *>>()["details"])
    }
}
