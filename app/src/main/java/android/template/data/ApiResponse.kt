package android.template.data

import android.template.data.local.database.Product
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class ApiResponse(
    @SerialName("products") var products: ArrayList<Product> = arrayListOf(),
)
