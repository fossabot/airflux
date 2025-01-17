package io.github.airflux.core.reader.validator.extension

import io.github.airflux.core.common.JsonErrors
import io.github.airflux.core.common.assertAsFailure
import io.github.airflux.core.common.assertAsSuccess
import io.github.airflux.core.lookup.JsLookup
import io.github.airflux.core.path.JsPath
import io.github.airflux.core.reader.JsReader
import io.github.airflux.core.reader.context.JsReaderContext
import io.github.airflux.core.reader.readRequired
import io.github.airflux.core.reader.result.JsErrors
import io.github.airflux.core.reader.result.JsLocation
import io.github.airflux.core.reader.result.JsResult
import io.github.airflux.core.reader.result.JsResult.Failure.Cause.Companion.bind
import io.github.airflux.core.reader.validator.JsPropertyValidator
import io.github.airflux.core.value.JsNull
import io.github.airflux.core.value.JsObject
import io.github.airflux.core.value.JsString
import io.github.airflux.core.value.JsValue
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class JsPropertyValidatorExtensionTest {

    companion object {
        private val context = JsReaderContext()
        private val isNotEmpty = JsPropertyValidator<String> { _, _, value ->
            if (value.isNotEmpty()) null else JsErrors.of(JsonErrors.Validation.Strings.IsEmpty)
        }

        val stringReader: JsReader<String> = JsReader { _, location, input ->
            when (input) {
                is JsString -> JsResult.Success(input.get, location)
                else -> JsResult.Failure(
                    location = location,
                    error = JsonErrors.InvalidType(expected = JsValue.Type.STRING, actual = input.type)
                )
            }
        }
    }

    @Nested
    inner class Reader {

        @Test
        fun `Testing of the extension-function the validation for JsReader`() {
            val json: JsValue = JsObject("name" to JsString("user"))
            val reader = JsReader { context, location, input ->
                val result = JsLookup.apply(location, JsPath("name"), input)
                readRequired(
                    from = result,
                    using = stringReader,
                    context = context,
                    pathMissingErrorBuilder = { JsonErrors.PathMissing },
                    invalidTypeErrorBuilder = JsonErrors::InvalidType
                )
            }.validation(isNotEmpty)

            val result = reader.read(context, JsLocation.Root, json)

            result.assertAsSuccess(location = JsLocation.Root / "name", value = "user")
        }

        @Test
        fun `Testing of the extension-function the validation for JsReader (error of validation)`() {
            val json: JsValue = JsObject("name" to JsString(""))
            val reader = JsReader { context, location, input ->
                val result = JsLookup.apply(location, JsPath("name"), input)
                readRequired(
                    from = result,
                    using = stringReader,
                    context = context,
                    pathMissingErrorBuilder = { JsonErrors.PathMissing },
                    invalidTypeErrorBuilder = JsonErrors::InvalidType
                )
            }.validation(isNotEmpty)

            val result = reader.read(context, JsLocation.Root, json)

            result.assertAsFailure("name" bind JsonErrors.Validation.Strings.IsEmpty)
        }

        @Test
        fun `Testing of the extension-function the validation for JsReader (result is failure)`() {
            val json: JsValue = JsObject("name" to JsNull)
            val reader = JsReader { context, location, input ->
                val result = JsLookup.apply(location, JsPath("name"), input)
                readRequired(
                    from = result,
                    using = stringReader,
                    context = context,
                    pathMissingErrorBuilder = { JsonErrors.PathMissing },
                    invalidTypeErrorBuilder = JsonErrors::InvalidType
                )
            }.validation(isNotEmpty)

            val result = reader.read(context, JsLocation.Root, json)

            result.assertAsFailure(
                "name" bind JsonErrors.InvalidType(expected = JsValue.Type.STRING, actual = JsValue.Type.NULL)
            )
        }
    }

    @Nested
    inner class Result {

        @Test
        fun `Testing of the extension-function the validation for JsResult`() {
            val result: JsResult<String> = JsResult.Success(location = JsLocation.Root / "name", value = "user")

            val validated = result.validation(isNotEmpty)

            validated.assertAsSuccess(location = JsLocation.Root / "name", value = "user")
        }

        @Test
        fun `Testing of the extension-function the validation for JsResult (error of validation)`() {
            val result: JsResult<String> = JsResult.Success(location = JsLocation.Root / "user", value = "")

            val validated = result.validation(isNotEmpty)

            validated.assertAsFailure("user" bind JsonErrors.Validation.Strings.IsEmpty)
        }

        @Test
        fun `Testing of the extension-function the validation for JsResult (result is failure)`() {
            val result: JsResult<String> =
                JsResult.Failure(location = JsLocation.Root / "user", error = JsonErrors.PathMissing)

            val validated = result.validation(isNotEmpty)

            validated.assertAsFailure("user" bind JsonErrors.PathMissing)
        }
    }
}
