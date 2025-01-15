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

package android.template.data

import android.template.data.local.database.Product
import android.template.data.local.database.ProductsDao
import android.template.data.local.database.ProductsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.map

interface ProductsRepository {
    val products: Flow<List<Product>>
    val favoriteProducts: Flow<List<Product>>

    suspend fun loadProducts()

    suspend fun updateFavorite(product: Product)
}

class DefaultProductsRepository
    @Inject
    constructor(
        private val productsDao: ProductsDao,
        private val client: HttpClient,
    ) : ProductsRepository {
        override val products: Flow<List<Product>> =
            productsDao.getProducts().map { items ->
                items.map {
                    Product(
                        id = it.id,
                        title = it.title,
                        price = it.price,
                        currency = it.currency,
                        image = it.image,
                        favorite = it.favorite,
                    )
                }
            }
        override val favoriteProducts: Flow<List<Product>> =
            productsDao.getMyFavorites().map { items ->
                items.map {
                    Product(
                        id = it.id,
                        title = it.title,
                        price = it.price,
                        currency = it.currency,
                        image = it.image,
                        favorite = it.favorite,
                    )
                }
            }

        // Fetch all of the the latest products and then update the products list
        override suspend fun loadProducts() {
            withContext(Dispatchers.IO) {
                val result =
                    kotlin.runCatching {
                        val response =
                            client.get("https://www.tradera.com/static/images/NO_REV/frontend-task/ProductFeedResult.json")

                        response
                            .body<ProductsResponse>()
                            .products
                            .map {
                                Product(
                                    id = it.id,
                                    title = it.title,
                                    price = it.price,
                                    currency = it.currency,
                                    image = it.image,
                                    favorite = false,
                                )
                            }
                    }
                if (result.isSuccess && result.getOrNull() != null) {
                    productsDao.insertProducts(items = result.getOrNull()!!)
                }
            }
        }

        // Invert value of favorite value of the currently selected product
        override suspend fun updateFavorite(product: Product) {
            productsDao.updateFavorite(id = product.id, favorite = !product.favorite)
        }
    }
