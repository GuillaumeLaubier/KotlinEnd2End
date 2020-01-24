package com.kotlinend2end.ultraquizzapi.model

import java.util.*

data class GameScore(val gamerName: String = "No Name", val score: Int = 0, val date: Date = Date())