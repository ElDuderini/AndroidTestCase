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

package android.template.data.di

import android.template.data.DefaultProductsRepository
import android.template.data.ProductsRepository
import android.template.data.local.database.Product
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindsProductsRepository(productsRepository: DefaultProductsRepository): ProductsRepository
}

class MockProductsRepository
    @Inject
    constructor() : ProductsRepository {
        override val products: Flow<List<Product>> = flowOf(mockProducts)
        override val favoriteProducts: Flow<List<Product>> = flowOf(mockProducts)

        override suspend fun loadProducts() {
            TODO("Not yet implemented")
        }

        override suspend fun updateFavorite(product: Product) {
            TODO("Not yet implemented")
        }
    }

val mockProducts =
    listOf(
        Product(
            id = 1001,
            title = "Back to the Future",
            price = 68,
            currency = "SEK",
            image = "https://upload.wikimedia.org/wikipedia/en/a/af/BackToTheFutureNESBoxart.jpg",
            favorite = false,
        ),
        Product(
            id = 1002,
            title = "Balloon Fight",
            price = 19,
            currency = "SEK",
            image = "https://upload.wikimedia.org/wikipedia/en/a/a8/BalloonFightnesboxart.jpg",
            favorite = true,
        ),
    )
