package io.github.airflux.core.reader.validator.base

import io.github.airflux.core.reader.result.JsError
import io.github.airflux.core.reader.result.JsErrors
import io.github.airflux.core.reader.validator.JsPropertyValidator

@Suppress("unused")
object BaseOrderValidators {

    /**
     * Validation of a value, if a value less than a [expected] value then [error], otherwise success.
     */
    fun <T> min(expected: T, error: (expected: T, actual: T) -> JsError): JsPropertyValidator<T>
        where T : Comparable<T> =
        JsPropertyValidator { _, _, value ->
            if (value < expected) JsErrors.of(error(expected, value)) else null
        }

    /**
     * Validation of a value, if a value more than a [expected] value then [error], otherwise success.
     */
    fun <T> max(expected: T, error: (expected: T, actual: T) -> JsError): JsPropertyValidator<T>
        where T : Comparable<T> =
        JsPropertyValidator { _, _, value ->
            if (value > expected) JsErrors.of(error(expected, value)) else null
        }

    /**
     * Validation of a value, if a value equal to a [expected] value then success, otherwise [error].
     */
    fun <T> eq(expected: T, error: (expected: T, actual: T) -> JsError): JsPropertyValidator<T>
        where T : Comparable<T> =
        JsPropertyValidator { _, _, value ->
            if (value == expected) null else JsErrors.of(error(expected, value))
        }

    /**
     * Validation of a value, if a value not equal to a [expected] value then success, otherwise [error].
     */
    fun <T> ne(expected: T, error: (expected: T, actual: T) -> JsError): JsPropertyValidator<T>
        where T : Comparable<T> =
        JsPropertyValidator { _, _, value ->
            if (value != expected) null else JsErrors.of(error(expected, value))
        }

    /**
     * Validation of a value, if a value greater than a [expected] value then success, otherwise [error].
     */
    fun <T> gt(expected: T, error: (expected: T, actual: T) -> JsError): JsPropertyValidator<T>
        where T : Comparable<T> =
        JsPropertyValidator { _, _, value ->
            if (value > expected) null else JsErrors.of(error(expected, value))
        }

    /**
     * Validation of a value, if a value greater than or equal to a [expected] value then success, otherwise [error].
     */
    fun <T> ge(expected: T, error: (expected: T, actual: T) -> JsError): JsPropertyValidator<T>
        where T : Comparable<T> =
        JsPropertyValidator { _, _, value ->
            if (value >= expected) null else JsErrors.of(error(expected, value))
        }

    /**
     * Validation of a value, if a value less than a [expected] value then success, otherwise [error].
     */
    fun <T> lt(expected: T, error: (expected: T, actual: T) -> JsError): JsPropertyValidator<T>
        where T : Comparable<T> =
        JsPropertyValidator { _, _, value ->
            if (value < expected) null else JsErrors.of(error(expected, value))
        }

    /**
     * Validation of a value, if a value less than or equal to a [expected] value then success, otherwise [error].
     */
    fun <T> le(expected: T, error: (expected: T, actual: T) -> JsError): JsPropertyValidator<T>
        where T : Comparable<T> =
        JsPropertyValidator { _, _, value ->
            if (value <= expected) null else JsErrors.of(error(expected, value))
        }
}
