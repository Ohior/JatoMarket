package jato.market.app.data_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("user_id") val uid: String,
    @SerialName("longitude") val longitude: Double = 0.0,
    @SerialName("latitude") val latitude: Double = 0.0,
    @SerialName("phone_number") val phoneNumber: String = "",
    @SerialName("document_id") val documentId: String = "",
    @SerialName("meta_data") val metaData: MetaData? = null,
    @SerialName("is_active") val isActive: Boolean = false,
    @SerialName("user_type") val userType: String = "",
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("store_document_uid") val storeDocumentId: String? = null,
    @SerialName("store") val store: StoreModel? = null,
) {
    companion object {
        fun empty() = UserModel(
            firstName = "",
            lastName = "",
            email = "",
            password = "",
            uid = ""
        )
    }
}

enum class UserType{
    TRADER,
    DEFAULT,
    USER,
    CUSTOMER,
    ADMIN,
    SUPER_ADMIN,
    DEVELOPER,
}

@Serializable
class MetaData

@Serializable
data class StoreModel(
    @SerialName("store_description") val storeDescription: String,
    @SerialName("store_name") val storeName: String,
    @SerialName("user_document_id") val userDocumentId: String,
    @SerialName("document_id") val documentId: String,
    @SerialName("store_id") val uid: String? = null,
    @SerialName("longitude") val longitude: Double = 0.0,
    @SerialName("latitude") val latitude: Double = 0.0,
    @SerialName("products") val products: List<ProductModel> = emptyList(),
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("meta_data") val metaData: MetaData? = null,
    @SerialName("is_active") val isActive: Boolean = false,
    @SerialName("sales") val sales: Int = 0,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
) {
    companion object {
        fun empty() = StoreModel(
            storeName = "",
            userDocumentId = "",
            storeDescription = "",
            documentId = ""
        )
    }
}

@Serializable
data class ProductModel(
    @SerialName("name")
    val productName: String,
    @SerialName("store_id")
    val storeId: String,
    @SerialName("product_id")
    val uid: String? = null,
    @SerialName("description")
    val productDescription: String,
    @SerialName("price")
    val productPrice: Double,
    @SerialName("quantity")
    val productQuantity: Int,
    @SerialName("image_url")
    val productImageUrls: List<String>? = null,
    @SerialName("summarys") val productSummarys: List<String>? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("meta_data") val metaData: MetaData = MetaData(),
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