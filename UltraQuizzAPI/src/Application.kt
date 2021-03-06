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
import io.ktor.request.receiveText
import java.io.FileReader
import java.text.DateFormat
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

            val numberOfRandomScore = call.parameters["number"]?.toInt() ?: 1
            val names: List<String> = listOf("Guigui", "lucroute", "emile", "marloute")

            for (i in (1..numberOfRandomScore)) {
                val newScore = GameScore(names[Random.nextInt(0, 4)], Random.nextInt(0, 101))
                scoreboard.submitScore(newScore)
            }

            call.respond(HttpStatusCode.Accepted, "$numberOfRandomScore Scores are stored")
        }

        /// Actual routes below

        get("/start") {
            // Select and return 5 random questions
            println("Route GET /start called.")
            call.respond(questionsList.shuffled().subList(0, 5))
        }

        post("/score") {
            println("Route POST /score called.")

            // response json formatted:
            // {
            //   "gamerName": <gamer_name>,
            //   "score" : <score_value>
            // }

            call.receiveText().let {
                val gameScore = Gson().fromJson(it, GameScore::class.java)

                if (scoreboard.submitScore(gameScore)) {
                    call.respond(
                        HttpStatusCode.Accepted,
                        "Score of ${gameScore.score} is stored for ${gameScore.gamerName}."
                    )
                } else {
                    call.respond(HttpStatusCode.NotAcceptable, "Score is not valid.")
                }
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

