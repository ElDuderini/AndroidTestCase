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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Unit tests for [DefaultProductsRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultProductRepositoryTest {
    @Test
    fun myModels_newItemSaved_itemIsReturned() =
        runTest {
            // val repository = DefaultProductsRepository(FakeProductDao())

            // repository.add("Repository")

            // assertEquals(repository.products.first().size, 1)
        }
}

private class FakeProductDao : ProductsDao {
    private val data = mutableListOf<Product>()

    override fun getProducts(): Flow<List<Product>> =
        flow {
            emit(data)
        }

    override fun getMyFavorites(favorite: Boolean): Flow<List<Product>> =
        flow {
            emit(data)
        }

    override suspend fun updateProduct(item: Product) {
    }

    override suspend fun insertProduct(item: Product) {
        TODO("Not yet implemented")
    }
}
