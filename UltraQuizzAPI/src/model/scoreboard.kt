package com.kotlinend2end.ultraquizzapi.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader

class ScoreBoard {

    private val scoreJsonFile = File("resources/scores.json")

    private val scoreList: ArrayList<GameScore> by lazy {
        val listGameScoreType = object : TypeToken<ArrayList<GameScore>>() {}.type
        val scores: ArrayList<GameScore> = Gson().fromJson(FileReader("resources/scores.json"), listGameScoreType)
        scores
    }

    fun submitScore(gameScore: GameScore) {
        scoreList.add(gameScore)

        scoreJsonFile.writeText(Gson().toJson(scoreList))
    }

    fun getBoard(): List<GameScore> = scoreList

    fun getLeaderBoard(): List<GameScore> = scoreList.sortedByDescending { it.score }.subList(0, 10)
}