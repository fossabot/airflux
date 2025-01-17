package io.github.airflux.core.writer.base

import io.github.airflux.core.value.JsNumber
import kotlin.test.Test
import kotlin.test.assertEquals

class BuilderLongWriterTest {

    companion object {
        private val longWriter = buildLongWriter()
    }

    @Test
    fun `Testing the writer for the Long type`() {
        val value: Long = Long.MAX_VALUE

        val result = longWriter.write(value)

        result as JsNumber
        assertEquals(value, result.get.toLong())
    }
}
