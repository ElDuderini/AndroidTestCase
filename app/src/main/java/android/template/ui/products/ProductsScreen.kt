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

import android.template.data.di.mockProducts
import android.template.data.local.database.Product
import android.template.ui.shared.ErrorScreen
import android.template.ui.shared.LoadingScreen
import android.template.ui.shared.ProductCard
import android.template.ui.theme.MyApplicationTheme
import android.template.ui.theme.Pink80
import android.template.ui.theme.Typography
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    when (items) {
        is ProductsUiState.Success ->
            ProductsScreen(
                products = (items as ProductsUiState.Success).data,
                modifier = modifier,
            )
        is ProductsUiState.Loading -> LoadingScreen()
        is ProductsUiState.Error -> ErrorScreen("Products were unable to be fetched")
    }
}

@Composable
internal fun ProductsScreen(
    products: List<Product>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        item {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .background(Pink80)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
            ) {
                Text("Products", style = Typography.titleLarge)
            }
        }

        item{
            Spacer(modifier = Modifier.height(24.dp))
        }

        items(products) { product ->
            ProductCard(product) {
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        ProductsScreen(mockProducts)
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        ProductsScreen(mockProducts)
    }
}
