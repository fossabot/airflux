package io.github.airflux.core.reader.validator.base

import io.github.airflux.core.reader.result.JsError
import io.github.airflux.core.reader.result.JsErrors
import io.github.airflux.core.reader.validator.JsPropertyValidator

@Suppress("unused")
object BaseArrayValidators {

    fun <T, C> minItems(expected: Int, error: (expected: Int, actual: Int) -> JsError): JsPropertyValidator<C>
        where C : Collection<T> =
        JsPropertyValidator { _, _, values ->
            if (values.size < expected) JsErrors.of(error(expected, values.size)) else null
        }

    fun <T, C> maxItems(expected: Int, error: (expected: Int, actual: Int) -> JsError): JsPropertyValidator<C>
        where C : Collection<T> =
        JsPropertyValidator { _, _, values ->
            if (values.size > expected) JsErrors.of(error(expected, values.size)) else null
        }

    fun <T, K> isUnique(
        failFast: Boolean,
        keySelector: (T) -> K,
        error: (index: Int, value: K) -> JsError
    ): JsPropertyValidator<Collection<T>> =
        JsPropertyValidator { _, _, values ->
            val errors = mutableListOf<JsError>()
            val unique = mutableSetOf<K>()
            values.forEachIndexed { index, item ->
                val key = keySelector(item)
                if (!unique.add(key)) errors.add(error(index, key))
                if (failFast && errors.isNotEmpty()) return@JsPropertyValidator JsErrors.of(errors)
            }
            JsErrors.of(errors)
        }
}
