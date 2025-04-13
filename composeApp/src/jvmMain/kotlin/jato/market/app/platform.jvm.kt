package jato.market.app

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.reflect.KClass


actual fun getJsonDatabase(): JsonDatabase {

//    val baseDir = File(System.getProperty("user.home"), ".myapp") // or any app folder
//    val baseDir = File(JsonDatabase::class.java.protectionDomain.codeSource.location.toURI()).parentFile
//    baseDir.mkdirs()

    return object : JsonDatabase {
        private fun getFilePath(tableName: String): File {
            val appDir = File(System.getProperty("user.home"), ".jatomarket/JatoMarket")
                .apply { mkdirs() }
            val file = File(appDir.path, tableName)
            if (!file.exists()) file.createNewFile()
            return file
        }

        override fun <T : Any> createData(tableName: String, data: KClass<T>): Boolean {
            return try {
                val json = Json.encodeToString(data)
                getFilePath(tableName).writeText(json)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

        }

        override fun <T : Any> updateData(tableName: String, data: KClass<T>): Boolean {
            return createData(tableName, data)
        }

        override fun deleteData(tableName: String): Boolean {
            return try {
                getFilePath(tableName).delete()
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        override fun getData(tableName: String): String? {
            return try {
                val file = getFilePath(tableName)
                file.readText()
            } catch (e: Exception) {
                Logger.w { e.toString() }
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
