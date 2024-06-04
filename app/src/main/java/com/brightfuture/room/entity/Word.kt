package com.brightfuture.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Word(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var phonetic: String = "",
    var audioLink: String = "",
    var definition: String = "",
    var example: String = "",
    var searched: Int = 0,
    var bookmark: Int = 0,
    var seen: Int = 0,
)