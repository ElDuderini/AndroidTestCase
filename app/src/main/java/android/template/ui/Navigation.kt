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

package android.template.ui

import android.template.ui.favorites.FavoritesScreen
import android.template.ui.products.ProductsScreen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable(
            "main",
            enterTransition = { slideIntoContainer() },
            exitTransition = { slideOutOfContainer() },
        ) {
            ProductsScreen(modifier = Modifier.padding(16.dp), navController = navController)
        }

        composable(
            "favorites",
            enterTransition = { slideIntoContainer() },
            exitTransition = { slideOutOfContainer() },
        ) {
            FavoritesScreen(modifier = Modifier.padding(16.dp), navController = navController)
        }
    }
}

fun slideIntoContainer(): EnterTransition =
    slideInVertically(
        animationSpec = tween(500, delayMillis = 90),
        initialOffsetY = { -it / 2 },
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))

fun slideOutOfContainer(): ExitTransition =
    slideOutVertically(
        animationSpec = tween(durationMillis = 500, delayMillis = 90),
        targetOffsetY = { -it / 2 },
    ) + fadeOut(tween(delayMillis = 90))
