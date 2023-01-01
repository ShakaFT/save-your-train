package com.example.save_your_train.data

import com.example.save_your_train.baseUrlApi
import com.example.save_your_train.ui.profile.AccountModel
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

suspend fun addRemoteAccount(account: AccountModel) {
    val payload: Map<String, Any> = mapOf(
        "email" to account.email,
        "firstName" to account.firstName,
        "lastName" to account.lastName,
        "password" to account.password,
    )
    callAPI("/account/add", "POST", payload)
}

suspend fun addRemoteExercise(exercise: Exercise, email: String) {
    val payload: Map<String, Any> = mapOf(
        "email" to email,
        "exercise" to exercise.toMap()
    )
    callAPI("/exercise/add", "POST", payload)
}

suspend fun addRemoteHistory(history: History, email: String) {
    val payload: Map<String, Any> = mapOf(
        "email" to email,
        "exercise" to history.toMap()
    )
    callAPI("/history/add", "POST", payload)
}

suspend fun removeRemoteExercise(exercise: Exercise, email: String) {
    val payload: Map<String, Any> = mapOf(
        "email" to email,
        "exerciseName" to exercise.name
    )
    callAPI("/exercise/delete", "POST", payload)
}

suspend fun removeRemoteHistory(history: History, email: String) {
    val payload: Map<String, Any> = mapOf(
        "email" to email,
        "dateMs" to history.dateMs
    )
    callAPI("/history/delete", "POST", payload)
}

suspend fun signInRemote(accountDataStore: AccountDataStore, account: AccountModel): Boolean {
    val payload: Map<String, Any> = mapOf(
        "email" to account.email,
        "password" to account.password,
    )
    val userData: Map<String, *> = callAPI("/account/sign_in", "POST", payload)
    // If false, email or password isn't correct
    if (!(userData["userSignIn"] as Boolean)) return false

    // Fill account data
    val account: Map<String, *> = userData["userData"] as Map<String, *>
    accountDataStore.setAccount(
        AccountModel(
            account["email"] as String,
            firstName = account["firstName"] as String,
            lastName = account["lastName"] as String
        )
    )

    // Fill exercises
    val exercises: List<Map<String, *>> = userData["exercises"] as List<Map<String, *>>
    val exerciseDao = AppDatabase.data!!.exerciseDao()

    for (exercise in exercises) {
        exerciseDao.insert(
            Exercise(
                exercise["exerciseName"] as String,
                exercise["description"] as String
            )
        )
    }

    // Fill history
    val histories: List<Map<String, *>> = userData["history"] as List<Map<String, *>>
    val historyDao = AppDatabase.data!!.historyDao()

    for (history in histories) {
        historyDao.insert(
            History(
                history["dateMs"] as Double,
                history["exerciseName"] as String,
                history["execution"] as String,
                history["repetition"] as String,
                history["rest"] as String,
                history["series"] as String,
                history["weight"] as String
            )
        )
    }
    return true
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
    // --------------------------------------------------------

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
            val map = (0 until value.length()).associate { it -> Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else            -> value
    }
}
