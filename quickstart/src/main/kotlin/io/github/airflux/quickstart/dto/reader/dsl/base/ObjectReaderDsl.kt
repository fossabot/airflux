package io.github.airflux.quickstart.dto.reader.dsl.base

import io.github.airflux.dsl.reader.`object`.ObjectReader
import io.github.airflux.dsl.reader.`object`.ObjectReaderConfiguration
import io.github.airflux.dsl.reader.`object`.validator.JsObjectValidators
import io.github.airflux.quickstart.dto.reader.base.ErrorBuilder
import io.github.airflux.quickstart.json.validation.additionalProperties
import io.github.airflux.quickstart.json.validation.isNotEmptyObject
import io.github.airflux.quickstart.json.validation.maxProperties
import io.github.airflux.quickstart.json.validation.minProperties

private val GlobalObjectReaderConfig = ObjectReaderConfiguration.build {
    failFast = true
}

private val GlobalObjectValidators = JsObjectValidators.build {
    +additionalProperties
    -additionalProperties

    +minProperties(10)
    -minProperties

    +maxProperties(25)
    -maxProperties

    +isNotEmptyObject
    -isNotEmptyObject
}

val reader = ObjectReader(
    globalConfiguration = GlobalObjectReaderConfig,
    globalValidators = GlobalObjectValidators,
    pathMissingErrorBuilder = ErrorBuilder.PathMissing,
    invalidTypeErrorBuilder = ErrorBuilder.InvalidType
)