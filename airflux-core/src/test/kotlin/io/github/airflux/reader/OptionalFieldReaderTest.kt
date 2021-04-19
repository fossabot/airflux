package io.github.airflux.reader

import io.github.airflux.common.JsonErrors
import io.github.airflux.common.TestData.USER_NAME_VALUE
import io.github.airflux.common.assertAsFailure
import io.github.airflux.common.assertAsSuccess
import io.github.airflux.lookup.JsLookup
import io.github.airflux.path.JsPath
import io.github.airflux.reader.result.JsResult
import io.github.airflux.value.JsNull
import io.github.airflux.value.JsObject
import io.github.airflux.value.JsString
import io.github.airflux.value.JsValue
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class OptionalFieldReaderTest {

    companion object {
        private val stringReader: JsReader<String, JsonErrors> =
            JsReader { input ->
                when (input) {
                    is JsString -> JsResult.Success(input.underlying)
                    else -> JsResult.Failure(
                        JsonErrors.InvalidType(expected = JsValue.Type.STRING, actual = input.type)
                    )
                }
            }
    }

    @Nested
    inner class FromJsLookup {

        @Test
        fun `Testing 'readOptional' function (an attribute is found)`() {
            val from: JsLookup = JsLookup.Defined(path = JsPath.empty / "name", JsString(USER_NAME_VALUE))

            val result: JsResult<String?, JsonErrors> =
                readOptional(from = from, using = stringReader, invalidTypeErrorBuilder = JsonErrors::InvalidType)

            result.assertAsSuccess(path = JsPath.empty / "name", value = USER_NAME_VALUE)
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is found with value 'null')`() {
            val from: JsLookup = JsLookup.Defined(path = JsPath.empty / "name", JsNull)

            val result: JsResult<String?, JsonErrors> =
                readOptional(from = from, using = stringReader, invalidTypeErrorBuilder = JsonErrors::InvalidType)

            result.assertAsFailure(
                JsPath.empty / "name" to listOf(
                    JsonErrors.InvalidType(expected = JsValue.Type.STRING, actual = JsValue.Type.NULL)
                )
            )
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is not found, returning value 'null')`() {
            val from: JsLookup = JsLookup.Undefined.PathMissing(path = JsPath.empty / "name")

            val result: JsResult<String?, JsonErrors> =
                readOptional(from = from, using = stringReader, invalidTypeErrorBuilder = JsonErrors::InvalidType)

            result.assertAsSuccess(path = JsPath.empty / "name", value = null)
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is not found, invalid type)`() {
            val from: JsLookup = JsLookup.Undefined.InvalidType(
                path = JsPath.empty / "name",
                expected = JsValue.Type.ARRAY,
                actual = JsValue.Type.STRING
            )

            val result: JsResult<String?, JsonErrors> =
                readOptional(from = from, using = stringReader, invalidTypeErrorBuilder = JsonErrors::InvalidType)

            result.assertAsFailure(
                JsPath.empty / "name" to listOf(
                    JsonErrors.InvalidType(expected = JsValue.Type.ARRAY, actual = JsValue.Type.STRING)
                )
            )
        }
    }

    @Nested
    inner class FromJsValueByPath {

        @Test
        fun `Testing 'readOptional' function (an attribute is found)`() {
            val json: JsValue = JsObject(
                "name" to JsString(USER_NAME_VALUE)
            )

            val result: JsResult<String?, JsonErrors> = readOptional(
                from = json,
                path = JsPath.empty / "name",
                using = stringReader,
                invalidTypeErrorBuilder = JsonErrors::InvalidType
            )

            result.assertAsSuccess(path = JsPath.empty / "name", value = USER_NAME_VALUE)
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is found with value 'null')`() {
            val json: JsValue = JsObject(
                "name" to JsNull
            )

            val result: JsResult<String?, JsonErrors> = readOptional(
                from = json,
                path = JsPath.empty / "name",
                using = stringReader,
                invalidTypeErrorBuilder = JsonErrors::InvalidType
            )

            result.assertAsFailure(
                JsPath.empty / "name" to listOf(
                    JsonErrors.InvalidType(expected = JsValue.Type.STRING, actual = JsValue.Type.NULL)
                )
            )
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is not found, returning value 'null')`() {
            val json: JsValue = JsObject(
                "name" to JsString(USER_NAME_VALUE)
            )

            val result: JsResult<String?, JsonErrors> = readOptional(
                from = json,
                path = JsPath.empty / "role",
                using = stringReader,
                invalidTypeErrorBuilder = JsonErrors::InvalidType
            )

            result.assertAsSuccess(path = JsPath.empty / "role", value = null)
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is not found, invalid type)`() {
            val json: JsValue = JsString(USER_NAME_VALUE)

            val result: JsResult<String?, JsonErrors> = readOptional(
                from = json,
                path = JsPath.empty / "name",
                using = stringReader,
                invalidTypeErrorBuilder = JsonErrors::InvalidType
            )

            result.assertAsFailure(
                JsPath.empty to listOf(
                    JsonErrors.InvalidType(expected = JsValue.Type.OBJECT, actual = JsValue.Type.STRING)
                )
            )
        }
    }

    @Nested
    inner class FromJsValueByName {

        @Test
        fun `Testing 'readOptional' function (an attribute is found)`() {
            val json: JsValue = JsObject(
                "name" to JsString(USER_NAME_VALUE)
            )

            val result: JsResult<String?, JsonErrors> = readOptional(
                from = json,
                name = "name",
                using = stringReader,
                invalidTypeErrorBuilder = JsonErrors::InvalidType
            )

            result.assertAsSuccess(path = JsPath.empty / "name", value = USER_NAME_VALUE)
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is found with value 'null')`() {
            val json: JsValue = JsObject(
                "name" to JsNull
            )

            val result: JsResult<String?, JsonErrors> = readOptional(
                from = json,
                name = "name",
                using = stringReader,
                invalidTypeErrorBuilder = JsonErrors::InvalidType
            )

            result.assertAsFailure(
                JsPath.empty / "name" to listOf(
                    JsonErrors.InvalidType(expected = JsValue.Type.STRING, actual = JsValue.Type.NULL)
                )
            )
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is not found, returning value 'null')`() {
            val json: JsValue = JsObject(
                "name" to JsString(USER_NAME_VALUE)
            )

            val result: JsResult<String?, JsonErrors> = readOptional(
                from = json,
                name = "role",
                using = stringReader,
                invalidTypeErrorBuilder = JsonErrors::InvalidType
            )

            result.assertAsSuccess(path = JsPath.empty / "role", value = null)
        }

        @Test
        fun `Testing 'readOptional' function (an attribute is not found, invalid type)`() {
            val json: JsValue = JsString(USER_NAME_VALUE)

            val result: JsResult<String?, JsonErrors> = readOptional(
                from = json,
                name = "name",
                using = stringReader,
                invalidTypeErrorBuilder = JsonErrors::InvalidType
            )

            result.assertAsFailure(
                JsPath.empty to listOf(
                    JsonErrors.InvalidType(expected = JsValue.Type.OBJECT, actual = JsValue.Type.STRING)
                )
            )
        }
    }
}
