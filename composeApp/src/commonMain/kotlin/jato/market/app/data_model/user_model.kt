package jato.market.app.data_model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val uid:String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val storeId: String? = null,
){
    companion object{
        fun empty() = UserModel(
            firstName = "",
            lastName = "",
            email = "",
            password = ""
        )
    }
}

@Serializable
data class StoreModel(
    val uid:String? = null,
    val storeName: String,
    val userId: String,
    val products: List<ProductModel> = emptyList(),
)

@Serializable
data class ProductModel(
    val uid:String? = null,
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val productQuantity: Int,
    val storeId: String,
)