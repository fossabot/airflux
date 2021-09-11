package io.github.airflux.sample.dto.reader.dsl

import io.github.airflux.dsl.reader.`object`.ObjectReaderConfiguration
import io.github.airflux.dsl.reader.`object`.validator.JsObjectValidators
import io.github.airflux.reader.result.asSuccess
import io.github.airflux.reader.validator.extension.validation
import io.github.airflux.sample.dto.model.Lot
import io.github.airflux.sample.dto.model.LotStatus
import io.github.airflux.sample.dto.reader.base.CollectionReader.list
import io.github.airflux.sample.dto.reader.base.PrimitiveReader.stringReader
import io.github.airflux.sample.dto.reader.base.asEnum
import io.github.airflux.sample.dto.reader.dsl.base.reader
import io.github.airflux.sample.json.validation.ArrayValidator.isUnique
import io.github.airflux.sample.json.validation.ArrayValidator.minItems
import io.github.airflux.sample.json.validation.StringValidator.isNotBlank
import io.github.airflux.sample.json.validation.additionalProperties

val LotStatusReader = stringReader.validation(isNotBlank).asEnum<LotStatus>()

private val LotObjectReaderConfig = ObjectReaderConfiguration.build {
    failFast = false
}

private val LotObjectValidators = JsObjectValidators.build {
    +additionalProperties
}

val LotReader = reader<Lot>(configuration = LotObjectReaderConfig, validators = LotObjectValidators) {
    val id = property(name = "id", reader = stringReader).required()
    val status = property(name = "status", reader = LotStatusReader).required()
    val value = property(name = "value", reader = ValueReader).required()

    build { path ->
        Lot(
            id = +id,
            status = +status,
            value = +value
        ).asSuccess(path)
    }
}

val LotsReader = list(LotReader)
    .validation(minItems<Lot, List<Lot>>(1) and isUnique { lot: Lot -> lot.id })