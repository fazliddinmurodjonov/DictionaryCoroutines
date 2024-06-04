package com.brightfuture.models.word_response

data class WordResponseItem(
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>,
    val word: String,
    val origin: String,
    val phonetic: String
)