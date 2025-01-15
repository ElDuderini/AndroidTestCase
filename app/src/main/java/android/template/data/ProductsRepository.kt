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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProductsRepository {
    val products: Flow<List<Product>>

    suspend fun add(product: Product)
}

class DefaultProductsRepository
    @Inject
    constructor(
        private val productsDao: ProductsDao,
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

        override suspend fun add(product: Product) {
            productsDao.insertProduct(product)
        }
    }
