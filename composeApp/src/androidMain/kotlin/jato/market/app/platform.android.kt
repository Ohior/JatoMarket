package jato.market.app

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.reflect.KClass

actual fun getJsonDatabase(): JsonDatabase {
    return object : JsonDatabase {
        override fun <T : Any> createData(tableName: String, data: KClass<T>): Boolean {
            return try {
                val jsonString = Json.encodeToString(data)
                AppActivity.instance.openFileOutput(tableName, Context.MODE_PRIVATE).use {
                    it.write(jsonString.toByteArray())
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        override fun <T : Any> updateData(tableName: String, data: KClass<T>): Boolean {
            // same as createData — just overwrite
            return createData(tableName, data)
        }

        override fun getData(tableName: String): String? {
            return try {
                val file = File(AppActivity.instance.filesDir, tableName)
                if (!file.exists()) return null
                file.readText()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun getDataFlow(tableName: String): Flow<String?> {
            return flow {
                val json = getData(tableName)
                emit(json)
            }
        }

        override fun deleteData(tableName: String): Boolean {
            return try {
                val file = File(AppActivity.instance.filesDir, tableName)
                file.delete()
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

    }
}