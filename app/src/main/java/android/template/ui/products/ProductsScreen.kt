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

import android.template.data.local.database.Product
import android.template.ui.shared.ErrorScreen
import android.template.ui.shared.LoadingScreen
import android.template.ui.shared.ProductCard
import android.template.ui.theme.MyApplicationTheme
import android.template.ui.theme.StarColor
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
fun ProductsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    when (val currentState = items) {
        is ProductsUiState.Success ->
            ProductsScreen(
                products = currentState.data,
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
            )
        is ProductsUiState.Loading -> LoadingScreen()
        is ProductsUiState.Error -> ErrorScreen("Products were unable to be fetched")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsScreen(
    products: List<Product>,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductsViewModel,
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
                    Text("Products")
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("favorites") },
            ) {
                Icon(Icons.Filled.Star, "Floating action button.", tint = StarColor)
            }
        },
    ) { padding ->
        LazyColumn(modifier.padding(padding)) {
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
        // ProductsScreen(products = mockProducts)
    }
}
