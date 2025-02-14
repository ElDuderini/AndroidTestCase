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

package android.template.ui.products

import android.template.data.ProductsRepository
import android.template.data.local.database.Product
import android.template.ui.products.ProductsUiState.Error
import android.template.ui.products.ProductsUiState.Loading
import android.template.ui.products.ProductsUiState.Success
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel
    @Inject
    constructor(
        private val productsRepository: ProductsRepository,
    ) : ViewModel() {
        val uiState: StateFlow<ProductsUiState> =
            productsRepository
                .products
                .map<List<Product>, ProductsUiState>(::Success)
                .catch { emit(Error(it)) }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

        init {
            viewModelScope.launch {
                productsRepository.loadProducts()
            }
        }

        fun updateFavoriteProduct(product: Product) {
            viewModelScope.launch {
                productsRepository.updateFavorite(product = product)
            }
        }
    }

sealed interface ProductsUiState {
    object Loading : ProductsUiState

    data class Error(
        val throwable: Throwable,
    ) : ProductsUiState

    data class Success(
        val data: List<Product>,
    ) : ProductsUiState
}
