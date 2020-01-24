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

    /**
     * Submit a score only if the given GameScore is valid. Return true if the submission
     * is done, false otherwise.
     */
    fun submitScore(gameScore: GameScore) : Boolean {
        // Check validity of GameScore
        if (gameScore.score < 0 || gameScore.score > 100) {
            // GameScore is valid only if the score is between 0 and 100.
            return false
        }

        scoreList.add(gameScore)
        scoreJsonFile.writeText(Gson().toJson(scoreList))

        return true
    }

    fun getBoard(): List<GameScore> = scoreList.sortedByDescending { it.date }

    fun getLeaderBoard(): List<GameScore> = scoreList.sortedByDescending { it.score }.run {
        if (size > 9) {
            subList(0, 10)
        } else {
            subList(0, size)
        }
    }
}