package com.kotlinend2end.ultraquizzapi

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotlinend2end.ultraquizzapi.model.Question
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.client.features.auth.basic.*
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
        get("/") {
            println("Route / called.")
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/gson") {
            println("Route /json/gson called.")
            call.respond(mapOf("hello" to "world"))
        }

        get("/json/questions") {
            println("Route /json/questions called.")
            call.respond(questionsList)
        }

        get("/json/questions/random") {
            println("Route /json/questions/random called.")
            call.respond(questionsList[Random.nextInt(0, questionsList.size)])
        }
    }
}

