package com.example.save_your_train.data

import com.example.save_your_train.baseUrlApi
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.errors.*
import org.json.JSONArray
import org.json.JSONObject

// Public functions

/*
lifecycleScope.launch {
    testAPI()
}
suspend fun testAPI() {
    val data: Map<String, *> = callAPI("/", "GET")
}
*/

// Private utils functions

private suspend fun callAPI(endpoint: String, methodRequest: String): Map<String, *> {
    val client = HttpClient(CIO) {
        install(ContentNegotiation){
            json()
        }
    }
    val response: HttpResponse = client.request("${baseUrlApi}${endpoint}") {
        method = getHttpMethod(methodRequest)
    }

    if (response.status.value !in 200..299) {
        throw IOException("An error occured during call to the API with endpoint : $endpoint")
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
