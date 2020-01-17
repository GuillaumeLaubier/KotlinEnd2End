package com.kotlinend2end.ultraquizzapi.model

import java.util.*

data class GameScore(val gamerName: String, val score: Int) {
    val date = Date()
}