package jato.market.app.database.local

import jato.market.app.data_model.ProductModel
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import jato.market.app.database.JsonDatabase
import jato.market.app.getJsonDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private const val USER_TABLE = "UserTable.json"

object JsonLocalDatabase : JsonDatabase {
    private val jsonDatabase = getJsonDatabase()

    override suspend fun getUserData(): UserModel? {
        val data = jsonDatabase.getData(USER_TABLE)
        return if (data.isNullOrEmpty()) null
        else {
            Json.decodeFromString<UserModel>(Json.decodeFromString<String>(data))
        }
    }

    override suspend fun getStoreData(): StoreModel? {
        val user = getUserData()
        return user?.store
    }

    override suspend fun getProductsData(): List<ProductModel>? {
        val user = getUserData()
        return user?.store?.products
    }

    override fun getUserDataFlow(): Flow<UserModel> {
        return jsonDatabase.getDataFlow(USER_TABLE).map {
            if (it.isNullOrEmpty()) UserModel.empty() else Json.decodeFromString<UserModel>(it)
        }
    }
}