package com.example.save_your_train.data

import com.example.save_your_train.baseUrlApi
import com.example.save_your_train.email
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.utils.io.errors.*
import org.json.JSONArray
import org.json.JSONObject


// Public functions

suspend fun addRemoteExercise(exercise: Exercise) {
    val payload: Map<String, Any> = mapOf(
        "email" to email,
        "exercise" to exercise.toMap()
    )
    callAPI("/exercise/add", "POST", payload)
}

suspend fun removeRemoteExercise(exercise: Exercise) {
    val payload: Map<String, Any> = mapOf(
        "email" to email,
        "exerciseName" to exercise.name
    )
    callAPI("/exercise/delete", "POST", payload)
}

// Private utils functions

@OptIn(InternalAPI::class)
private suspend fun callAPI(endpoint: String, methodRequest: String, payload: Map<String, Any>? = null): Map<String, *> {
    val client = HttpClient(CIO) {
        install(ContentNegotiation){
            json()
        }
    }
    val response: HttpResponse = client.request("${baseUrlApi}${endpoint}") {
        method = getHttpMethod(methodRequest)
        if (payload != null) {
            contentType(ContentType.Application.Json)
            body = Gson().toJson(payload)
        }
    }

    if (response.status.value !in 200..299) {
        println(response.bodyAsText()) // Display error in console to debug
        throw IOException("An error occurred during call to the API with endpoint : $endpoint")
    }
    var dataString: String = "" +  response.bodyAsText()

    // fix bug when API returns null, dataString equals "null\n"
    dataString = dataString.replace("\n", "")

    return if (dataString == "null") mapOf<String, Any>() else JSONObject(dataString).toMap()
}

private fun getHttpMethod(method: String): HttpMethod {
    return when (method) {
        "GET" -> HttpMethod.Get
        "POST" -> HttpMethod.Post
        "PUT" -> HttpMethod.Put
        "DELETE" -> HttpMethod.Delete
        else -> { // Note the block
            throw IOException("Unknown method $method")
        }
    }
}

// This function add toMap() method for JSONObject
private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it])
    {
        is JSONArray ->
        {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else            -> value
    }
}
