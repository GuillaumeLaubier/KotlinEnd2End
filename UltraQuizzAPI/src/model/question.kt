package com.kotlinend2end.ultraquizzapi.model

data class Question(val sentence: String, val answer: String, val otherChoices: List<String>)