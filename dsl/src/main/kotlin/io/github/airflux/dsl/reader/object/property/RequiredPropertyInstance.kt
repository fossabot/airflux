package io.github.airflux.dsl.reader.`object`.property

import io.github.airflux.core.lookup.JsLookup
import io.github.airflux.core.path.JsPath
import io.github.airflux.core.reader.JsReader
import io.github.airflux.core.reader.context.JsReaderContext
import io.github.airflux.core.reader.error.InvalidTypeErrorBuilder
import io.github.airflux.core.reader.error.PathMissingErrorBuilder
import io.github.airflux.core.reader.readRequired
import io.github.airflux.core.reader.result.JsLocation
import io.github.airflux.core.reader.result.JsResult
import io.github.airflux.core.reader.validator.JsPropertyValidator
import io.github.airflux.core.reader.validator.extension.validation
import io.github.airflux.core.value.JsValue

internal class RequiredPropertyInstance<T : Any> private constructor(
    override val path: JsPath,
    private var reader: JsReader<T>
) : RequiredProperty<T> {

    companion object {

        fun <T : Any> of(
            path: JsPath,
            reader: JsReader<T>,
            pathMissingErrorBuilder: PathMissingErrorBuilder,
            invalidTypeErrorBuilder: InvalidTypeErrorBuilder
        ): RequiredProperty<T> =
            RequiredPropertyInstance(path) { context, location, input ->
                val lookup = JsLookup.apply(location, path, input)
                readRequired(context, lookup, reader, pathMissingErrorBuilder, invalidTypeErrorBuilder)
            }
    }

    override fun read(context: JsReaderContext, location: JsLocation, input: JsValue): JsResult<T> =
        reader.read(context, location, input)

    override fun validation(validator: JsPropertyValidator<T>): RequiredPropertyInstance<T> {
        val previousReader = this.reader
        reader = JsReader { context, location, input ->
            previousReader.read(context, location, input).validation(context, validator)
        }
        return this
    }
}
