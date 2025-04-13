package jato.market.app.database

import jato.market.app.data_model.UserModel
import jato.market.app.getJsonDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private const val USER_TABLE = "UserTable.json"

class JsonLocalDatabase {
    private val jsonDatabase = getJsonDatabase()

    fun getUserData(): UserModel? {
        val data = jsonDatabase.getData(USER_TABLE)
        return if (data.isNullOrEmpty()) null else Json.decodeFromString(data)
    }

    fun getUserDataFlow(): Flow<UserModel?> {
        return jsonDatabase.getDataFlow(USER_TABLE).map {
            if (it.isNullOrEmpty()) null else Json.decodeFromString<UserModel>(it)
        }
    }
}