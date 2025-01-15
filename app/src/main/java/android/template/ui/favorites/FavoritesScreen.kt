package android.template.ui.favorites

import android.template.data.local.database.Product
import android.template.ui.shared.ErrorScreen
import android.template.ui.shared.LoadingScreen
import android.template.ui.shared.ProductCard
import android.template.ui.theme.MyApplicationTheme
import android.template.ui.theme.Typography
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController,
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    when (items) {
        is FavoritesUiState.Success ->
            FavoritesScreen(
                products = (items as FavoritesUiState.Success).data,
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
            )
        is FavoritesUiState.Loading -> LoadingScreen()
        is FavoritesUiState.Error -> ErrorScreen("You have not saved any favorites yet")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesScreen(
    products: List<Product>,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors =
                    topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                title = {
                    Text("Favorites")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Return back to the products page",
                        )
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(modifier.padding(padding)) {
            if (products.isEmpty()) {
                item {
                    Text("No favorites have been saved yet.", style = Typography.titleMedium)
                }
            }
            items(products) { product ->
                Spacer(Modifier.height(24.dp))
                ProductCard(product) {
                    viewModel.updateFavoriteProduct(product)
                }
            }
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        // FavoritesScreen(mockProducts)
    }
}
