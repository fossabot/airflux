package io.github.airflux.reader

import io.github.airflux.common.JsonErrors
import io.github.airflux.common.TestData.FIRST_PHONE_VALUE
import io.github.airflux.common.TestData.SECOND_PHONE_VALUE
import io.github.airflux.common.TestData.USER_NAME_VALUE
import io.github.airflux.path.JsPath
import io.github.airflux.reader.TraversableReader.Companion.list
import io.github.airflux.reader.TraversableReader.Companion.set
import io.github.airflux.reader.result.JsResult
import io.github.airflux.value.JsArray
import io.github.airflux.value.JsBoolean
import io.github.airflux.value.JsNumber
import io.github.airflux.value.JsString
import io.github.airflux.value.JsValue
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class TraversableReaderTest {

    companion object {
        private val stringReader: JsReader<String> = JsReader { input ->
            when (input) {
                is JsString -> JsResult.Success(input.underlying)
                else -> JsResult.Failure(
                    error = JsonErrors.InvalidType(expected = JsValue.Type.STRING, actual = input.type)
                )
            }
        }
    }

    @Nested
    inner class ListReader {

        @Test
        fun `Testing 'list' function`() {
            val json: JsValue = JsArray(JsString(FIRST_PHONE_VALUE), JsString(SECOND_PHONE_VALUE))
            val reader: JsReader<List<String>> = list(using = stringReader, errorInvalidType = JsonErrors::InvalidType)

            val result: JsResult<List<String>> = reader.read(json)

            result as JsResult.Success
            assertEquals(JsPath.empty, result.path)
            assertEquals(listOf(FIRST_PHONE_VALUE, SECOND_PHONE_VALUE), result.value)
        }

        @Test
        fun `Testing 'list' function (an attribute is not collection)`() {
            val json: JsValue = JsString(USER_NAME_VALUE)
            val reader: JsReader<List<String>> = list(using = stringReader, errorInvalidType = JsonErrors::InvalidType)

            val result: JsResult<List<String>> = reader.read(json)

            result as JsResult.Failure
            assertEquals(1, result.errors.size)
            result.errors[0]
                .also { (pathError, errors) ->
                    assertEquals(JsPath.empty, pathError)

                    assertEquals(1, errors.size)
                    val error = errors[0] as JsonErrors.InvalidType
                    assertEquals(JsValue.Type.ARRAY, error.expected)
                    assertEquals(JsValue.Type.STRING, error.actual)
                }
        }

        @Test
        fun `Testing 'list' function (collection with inconsistent content)`() {
            val json: JsValue = JsArray(
                JsString(FIRST_PHONE_VALUE),
                JsNumber.valueOf(10),
                JsBoolean.True,
                JsString(SECOND_PHONE_VALUE)
            )
            val reader: JsReader<List<String>> = list(using = stringReader, errorInvalidType = JsonErrors::InvalidType)

            val result: JsResult<List<String>> = reader.read(json)

            result as JsResult.Failure
            assertEquals(2, result.errors.size)

            result.errors[0]
                .also { (pathError, errors) ->
                    assertEquals(JsPath.empty / 1, pathError)
                    assertEquals(1, errors.size)
                }

            result.errors[1]
                .also { (pathError, errors) ->
                    assertEquals(JsPath.empty / 2, pathError)
                    assertEquals(1, errors.size)
                }
        }

        @Test
        fun `Testing 'list' function (array is empty)`() {
            val json: JsValue = JsArray<JsString>()

            val reader: JsReader<List<String>> = list(using = stringReader, errorInvalidType = JsonErrors::InvalidType)

            val result: JsResult<List<String>> = reader.read(json)

            result as JsResult.Success
            assertEquals(JsPath.empty, result.path)
            assertEquals(listOf(), result.value)
        }
    }

    @Nested
    inner class SetReader {

        @Test
        fun `Testing 'set' function`() {
            val json: JsValue = JsArray(JsString(FIRST_PHONE_VALUE), JsString(SECOND_PHONE_VALUE))
            val reader: JsReader<Set<String>> = set(using = stringReader, errorInvalidType = JsonErrors::InvalidType)

            val result: JsResult<Set<String>> = reader.read(json)

            result as JsResult.Success
            assertEquals(JsPath.empty, result.path)
            assertEquals(setOf(FIRST_PHONE_VALUE, SECOND_PHONE_VALUE), result.value)
        }

        @Test
        fun `Testing 'set' function (an attribute is not collection)`() {
            val json: JsValue = JsString(USER_NAME_VALUE)
            val reader: JsReader<Set<String>> = set(using = stringReader, errorInvalidType = JsonErrors::InvalidType)

            val result: JsResult<Set<String>> = reader.read(json)

            result as JsResult.Failure
            assertEquals(1, result.errors.size)
            result.errors[0]
                .also { (pathError, errors) ->
                    assertEquals(JsPath.empty, pathError)

                    assertEquals(1, errors.size)
                    val error = errors[0] as JsonErrors.InvalidType
                    assertEquals(JsValue.Type.ARRAY, error.expected)
                    assertEquals(JsValue.Type.STRING, error.actual)
                }
        }

        @Test
        fun `Testing 'set' function (collection with inconsistent content)`() {
            val json: JsValue = JsArray(
                JsString(FIRST_PHONE_VALUE),
                JsNumber.valueOf(10),
                JsBoolean.True,
                JsString(SECOND_PHONE_VALUE)
            )
            val reader: JsReader<Set<String>> = set(using = stringReader, errorInvalidType = JsonErrors::InvalidType)

            val result: JsResult<Set<String>> = reader.read(json)

            result as JsResult.Failure
            assertEquals(2, result.errors.size)

            result.errors[0]
                .also { (pathError, errors) ->
                    assertEquals(JsPath.empty / 1, pathError)
                    assertEquals(1, errors.size)
                }

            result.errors[1]
                .also { (pathError, errors) ->
                    assertEquals(JsPath.empty / 2, pathError)
                    assertEquals(1, errors.size)
                }
        }

        @Test
        fun `Testing 'set' function (array is empty)`() {
            val json: JsValue = JsArray<JsString>()
            val reader: JsReader<Set<String>> = set(using = stringReader, errorInvalidType = JsonErrors::InvalidType)

            val result: JsResult<Set<String>> = reader.read(json)

            result as JsResult.Success
            assertEquals(JsPath.empty, result.path)
            assertEquals(setOf(), result.value)
        }
    }
}