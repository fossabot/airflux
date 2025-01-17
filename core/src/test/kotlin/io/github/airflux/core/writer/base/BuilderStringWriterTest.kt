package io.github.airflux.core.writer.base

import io.github.airflux.core.value.JsString
import kotlin.test.Test
import kotlin.test.assertEquals

class BuilderStringWriterTest {

    companion object {
        private val stringWriter = buildStringWriter()
    }

    @Test
    fun `Testing the writer for String type`() {
        val value = "abc"

        val result = stringWriter.write(value)

        result as JsString
        assertEquals(value, result.get)
    }
}
