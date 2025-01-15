package android.template.ui.shared

import android.template.R
import android.template.data.di.mockProducts
import android.template.data.local.database.Product
import android.template.ui.theme.MyApplicationTheme
import android.template.ui.theme.StarColor
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ProductCard(
    product: Product,
    onFavoriteClick: () -> Unit,
) {
    val context = LocalContext.current

    OutlinedCard(modifier = Modifier.padding(horizontal = 24.dp)) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(context)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
            contentDescription = "Cover art of: ${product.title}",
            placeholder = debugPlaceholder(R.drawable.ic_launcher_background),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .aspectRatio(16 / 9f)
                    .fillMaxWidth(),
        )
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column {
                Text("${product.title}")
                Text("${product.price} ${product.currency}")
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onFavoriteClick) {
                val icon =
                    if (product.favorite) {
                        Icons.Filled.Star
                    } else {
                        Icons.Outlined.Star
                    }
                Icon(icon, modifier = Modifier.size(40.dp), contentDescription = "Toggle favorite, current set to ${product.favorite}", tint = StarColor)
            }
        }
    }
}

@Composable
fun debugPlaceholder(
    @DrawableRes debugPreview: Int,
) = if (LocalInspectionMode.current) {
    painterResource(id = debugPreview)
} else {
    null
}

// Previews
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        ProductCard(mockProducts.first(), onFavoriteClick = {})
    }
}
