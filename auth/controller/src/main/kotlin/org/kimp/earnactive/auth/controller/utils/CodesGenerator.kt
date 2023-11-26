package org.kimp.earnactive.auth.controller.utils

class CodesGenerator (
    private val codeLength: Int = 6,
    private val alphabet: String = "0123456789"
) {
    fun generateCode(): String = buildString {
        repeat(codeLength) { append(getRandomCharacterFromAlphabet()) }
    }

    private fun getRandomCharacterFromAlphabet() = alphabet.asSequence()
        .shuffled().find { true }!!
}
