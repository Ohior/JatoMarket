package jato.market.app

import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

actual fun getJsonDatabase(): JsonDatabase {

    return object : JsonDatabase {
        override fun <T : Any> createData(tableName: String, data: KClass<T>): Boolean {
            return try {
                val json = Json.encodeToString(data)
                localStorage.setItem(tableName, json)
                true
            } catch (e: Exception) {
                console.error("createData error", e)
                false
            }
        }

        override fun <T : Any> updateData(tableName: String, data: KClass<T>): Boolean {
            return createData(tableName, data)
        }

        override fun deleteData(tableName: String): Boolean {
            return try {
                localStorage.removeItem(tableName)
                true
            } catch (e: Exception) {
                console.error("deleteData error", e)
                false
            }
        }

        override fun getData(tableName: String): String? {
            return try {
                val jsonString = localStorage.getItem(tableName) ?: return null
                jsonString
            } catch (e: Exception) {
                console.error("readRawJson error", e)
                null
            }
        }

        override fun getDataFlow(tableName: String): Flow<String?> {
            return flow {
                val json = getData(tableName)
                emit(json)
            }
        }
    }
}
