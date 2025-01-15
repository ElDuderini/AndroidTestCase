package android.template.ui.favorites

import android.template.data.ProductsRepository
import android.template.data.local.database.Product
import android.template.ui.favorites.FavoritesUiState.Error
import android.template.ui.favorites.FavoritesUiState.Loading
import android.template.ui.favorites.FavoritesUiState.Success
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
class FavoritesViewModel
    @Inject
    constructor(
        private val productsRepository: ProductsRepository,
    ) : ViewModel() {
        val uiState: StateFlow<FavoritesUiState> =
            productsRepository
                .favoriteProducts
                .map<List<Product>, FavoritesUiState>(::Success)
                .catch { emit(Error(it)) }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

        fun updateFavoriteProduct(product: Product) {
            viewModelScope.launch {
                productsRepository.updateFavorite(product = product)
            }
        }
    }

sealed interface FavoritesUiState {
    object Loading : FavoritesUiState

    data class Error(
        val throwable: Throwable,
    ) : FavoritesUiState

    data class Success(
        val data: List<Product>,
    ) : FavoritesUiState
}
