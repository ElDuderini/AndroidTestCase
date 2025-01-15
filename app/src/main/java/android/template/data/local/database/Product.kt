/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.template.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    @SerialName("products") var products: ArrayList<Product> = arrayListOf(),
)

@Serializable @Entity
data class Product(
    @PrimaryKey
    @SerialName("id") var id: Int,
    @SerialName("title") var title: String,
    @SerialName("price") var price: Int,
    @SerialName("currency") var currency: String,
    @SerialName("image") var image: String,
    val favorite: Boolean = false,
)

@Dao
interface ProductsDao {
    @Query("SELECT * FROM product ORDER BY title")
    fun getProducts(): Flow<List<Product>>

    // Fetch products that are favorites
    @Query("SELECT * FROM product WHERE favorite = :favorite ORDER BY title")
    fun getMyFavorites(favorite: Boolean = true): Flow<List<Product>>

    // Update favorite status on a specific product
    @Query("UPDATE product SET favorite=:favorite WHERE id = :id")
    suspend fun updateFavorite(
        id: Int,
        favorite: Boolean,
    )

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProducts(items: List<Product>)

    @Query("SELECT favorite FROM product WHERE id = :id")
    suspend fun getFavoriteState(id: Int): Boolean
}
