package jato.market.app.database.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import jato.market.app.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json


object JatoDatabase {
    private val remoteMarketApi = RemoteMarketApi()

    suspend fun getUserData(documentId: String): UserModel? {
        return remoteMarketApi.getData<UserModel>("users/$documentId")
    }

    suspend fun updateUserData(userModel: UserModel): Boolean {
        return remoteMarketApi.postData("users/${userModel.documentId}", userModel)
    }


    fun getAllStoresFlow(): Flow<List<StoreModel>> {
        return flow {
            val stores = remoteMarketApi.getData<List<StoreModel>>("stores")
            if (stores != null) {
                emit(stores)
            }
        }
    }

    suspend fun getStoreData(documentId: String): StoreModel? {
        return remoteMarketApi.getData<StoreModel>("stores/$documentId")
    }

    suspend fun updateStoreData(storeModel: StoreModel): Boolean {
        return remoteMarketApi.postData("stores/${storeModel.documentId}", storeModel)
    }

    suspend fun isNetworkAvailable(): Boolean {
        return remoteMarketApi.isNetworkAvailable()
    }
}

private class RemoteMarketApi {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                prettyPrint = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("HTTP Client: $message")
                }
            }
            level = LogLevel.INFO // Or BODY for more detailed logging
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000  // total timeout per request
            connectTimeoutMillis = 5_000   // timeout to establish TCP connection
            socketTimeoutMillis = 10_000   // timeout for inactivity on socket
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            retryOnException(maxRetries = 3)
            exponentialDelay()
            retryIf { request, response ->
                if (!response.status.isSuccess() && response.status.value != 404) {
                    co.touchlab.kermit.Logger.e("Retrying request to ${request.url} due to status ${response.status}")
                    true
                } else {
                    false
                }
            }
        }
    }

    suspend inline fun <reified T> getData(valuePath: String): T? {
        try {
            val response: HttpResponse = client.get(Constants.API_ROOT_URL + valuePath + "/") {
                headers {
//                    append(HttpHeaders.Authorization, "Bearer your_api_token")
                    append(Constants.API_NAME, Constants.API_VALUE)
                    append(HttpHeaders.ContentType, "application/json")

                }
            }
            val json = Json {
                ignoreUnknownKeys = true  // <- This is the fix!
                explicitNulls = false
                prettyPrint = true
                isLenient = true
                encodeDefaults = true

            }
            return if (response.status.isSuccess()) {
                json.decodeFromString(response.body<String>())
            } else {
                co.touchlab.kermit.Logger.e { "Request failed with status: ${response.status.description}" }
                null
            }
        } catch (e: Exception) {
            co.touchlab.kermit.Logger.e { "Error during API call: ${e.message}" }
            return null
        }
    }

    suspend inline fun <reified T> postData(valuePath: String, data: T): Boolean {
        try {
            val response = client.post(Constants.API_ROOT_URL + "/" + valuePath) {
                headers {
//                    append(HttpHeaders.Authorization, "Bearer your_api_token")
                    append(Constants.API_NAME, Constants.API_VALUE)
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody(Json.encodeToString<T>(data))
            }
            return response.status.isSuccess()
        } catch (e: Exception) {
            co.touchlab.kermit.Logger.e { "Error during API call: ${e.message}" }
            return false
        }
    }

    suspend inline fun isNetworkAvailable(testUrl: String = "https://www.google.com"): Boolean {
        return try {
            val response = client.get(testUrl)
            response.status.isSuccess()
        } catch (e: Exception) {
            co.touchlab.kermit.Logger.e { "Network is not available : ${e.message}" }
            false
        }
    }
}