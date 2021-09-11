package io.github.airflux.sample.dto.reader.dsl

import io.github.airflux.reader.result.asSuccess
import io.github.airflux.sample.dto.model.Tender
import io.github.airflux.sample.dto.reader.base.PrimitiveReader.stringReader
import io.github.airflux.sample.dto.reader.dsl.base.reader
import io.github.airflux.sample.json.validation.StringValidator.isNotBlank

val TenderReader = reader<Tender> {
    val id = property(name = "id", reader = stringReader).required().validation(isNotBlank)
    val title = property(name = "title", reader = TitleReader).optional()
    val value = property(name = "value", reader = ValueReader).optional()
    val lots = property(name = "lots", reader = LotsReader).required()

    build { path ->
        Tender(
            id = this[id],
            title = this[title],
            value = this[value],
            lots = this[lots],
        ).asSuccess(path)
    }
}