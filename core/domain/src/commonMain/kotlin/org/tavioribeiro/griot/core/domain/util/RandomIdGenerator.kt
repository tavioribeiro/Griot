package org.tavioribeiro.griot.core.domain.util

import kotlin.random.Random

object RandomIdGenerator {

    fun next(): String {
        val bytes = ByteArray(16)
        Random.nextBytes(bytes)
        return bytes.joinToString(separator = "") { byte ->
            (byte.toInt() and 0xFF).toString(16).padStart(2, '0')
        }
    }
}