package jato.market.app

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass


interface JsonDatabase {
    fun <T : Any> createData(tableName: String, data: KClass<T>): Boolean
    fun <T : Any> updateData(tableName: String, data: KClass<T>): Boolean
    fun getData(tableName: String): String?
    fun getDataFlow(tableName: String): Flow<String?>
    fun deleteData(tableName: String): Boolean
}

expect fun getJMapManager(): JMapManager

expect fun getJsonDatabase(): JsonDatabase


//return Location(6.34, 5.62) // Example: Benin City mock location
