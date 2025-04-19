package jato.market.app.data_model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val imageUrl:String? = null,
    val uid: String? = null,
    val storeUid: String? = null,
    val store: StoreModel? = null,
) {
    companion object {
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
    val storeName: String,
    val storeDescription: String,
    val userId: String,
    val imageUrl:String? = null,
    val uid: String? = null,
    val products: List<ProductModel> = emptyList(),
) {
    companion object {
        fun empty() = StoreModel(
            storeName = "",
            userId = "",
            storeDescription = ""
        )
    }
}

@Serializable
data class ProductModel(
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val productQuantity: Int,
    val storeId: String,
    val uid: String? = null,
    val productImageUrls: List<String>? = null,
    val productSummarys: List<String>? = null,
) {
    companion object {
        fun empty() = ProductModel(
            productName = "",
            productDescription = "",
            productPrice = 0.0,
            productQuantity = 0,
            storeId = ""
        )
    }
}