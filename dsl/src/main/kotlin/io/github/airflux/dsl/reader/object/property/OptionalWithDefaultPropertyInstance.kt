package io.github.airflux.dsl.reader.`object`.property

import io.github.airflux.core.lookup.JsLookup
import io.github.airflux.core.path.JsPath
import io.github.airflux.core.reader.JsReader
import io.github.airflux.core.reader.context.JsReaderContext
import io.github.airflux.core.reader.error.InvalidTypeErrorBuilder
import io.github.airflux.core.reader.readOptional
import io.github.airflux.core.reader.result.JsLocation
import io.github.airflux.core.reader.result.JsResult
import io.github.airflux.core.reader.validator.JsPropertyValidator
import io.github.airflux.core.reader.validator.extension.validation
import io.github.airflux.core.value.JsValue

internal class OptionalWithDefaultPropertyInstance<T : Any> private constructor(
    override val path: JsPath,
    private var reader: JsReader<T>,
) : OptionalWithDefaultProperty<T> {

    companion object {

        fun <T : Any> of(
            path: JsPath,
            reader: JsReader<T>,
            default: () -> T,
            invalidTypeErrorBuilder: InvalidTypeErrorBuilder
        ): OptionalWithDefaultProperty<T> =
            OptionalWithDefaultPropertyInstance(path) { context, location, input ->
                val lookup = JsLookup.apply(location, path, input)
                readOptional(context, lookup, reader, default, invalidTypeErrorBuilder)
            }
    }

    override fun read(context: JsReaderContext, location: JsLocation, input: JsValue): JsResult<T> =
        reader.read(context, location, input)

    override fun validation(validator: JsPropertyValidator<T>): OptionalWithDefaultPropertyInstance<T> {
        val previousReader = this.reader
        reader = JsReader { context, location, input ->
            previousReader.read(context, location, input).validation(context, validator)
        }
        return this
    }
}
