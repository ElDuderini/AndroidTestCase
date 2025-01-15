package android.template.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun ErrorScreen(message: String) {
    Box(contentAlignment = Alignment.Center) {
        Text(message)
    }
}
