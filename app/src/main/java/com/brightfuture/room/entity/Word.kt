package com.brightfuture.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Word(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String? = "",
    val phonetic: String? = "",
    val audioLink: String? = "",
    val definition: String? = "",
    val example: String? = "",
    val searched: Int? = 0,
    val bookmark: Int = 0,
    val seen: Int? = 0,
)