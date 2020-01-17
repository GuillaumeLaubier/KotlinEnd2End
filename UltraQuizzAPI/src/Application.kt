package com.kotlinend2end.ultraquizzapi

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotlinend2end.ultraquizzapi.model.GameScore
import com.kotlinend2end.ultraquizzapi.model.Question
import com.kotlinend2end.ultraquizzapi.model.ScoreBoard
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.request.receive
import io.ktor.request.receiveText
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.DateFormat
import java.util.*
import kotlin.random.Random

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// will deserialize all questions at first call.
private val questionsList: List<Question> by lazy {
    val listQuestionType = object : TypeToken<List<Question>>() {}.type
    val questions: List<Question> = Gson().fromJson(FileReader("resources/questions.json"), listQuestionType)
    questions
}

private val scoreboard = ScoreBoard()

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // TODO maybe later
    //install(Authentication) {
    //}

    install(DefaultHeaders)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    /* TODO maybe later
    val client = HttpClient(Apache) {
        install(BasicAuth) {
            username = "test"
            password = "pass"
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }
    }*/

    routing {

        /// Debug routes below

        get("/") {
            println("Route GET / called.")
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/questions") {
            println("Route GET /json/questions called.")
            call.respond(questionsList)
        }

        post("/randomscore") {
            println("Route POST /randomscore called.")

            // response json formatted:
            // {
            //   "name": <gamer_name>,
            //   "score" : <score_value>
            // }


            // put fix score for testing
            val names: List<String> = listOf("Guigui", "lucroute", "emile", "marloute")
            val newScore = GameScore(names[Random.nextInt(0, 4)], Random.nextInt(0, 6))

            scoreboard.submitScore(newScore)

            call.respond(HttpStatusCode.Accepted, "Score is stored")
        }

        /// Actual routes below

        get("/start") {
            // Select and return 5 random questions
            println("Route GET /start called.")
            call.respond(questionsList.shuffled().subList(0, 5))
        }

        post("/score") {
            println("Route POST /score called.")

            call.receiveText().let {
                val gameScore = Gson().fromJson(it, GameScore::class.java)

                scoreboard.submitScore(gameScore)

                call.respond(HttpStatusCode.Accepted, "Score of ${gameScore.score} is stored for ${gameScore.gamerName}.")
            }
        }

        get("/scoreboard") {
            println("Route GET /scoreboard called.")
            // return actual scoreboard
            call.respond(scoreboard.getBoard())
        }

        get("/leaderboard") {
            println("Route GET /leaderboard called.")
            // Return 10 bests score
            call.respond(scoreboard.getLeaderBoard())
        }
    }
}

