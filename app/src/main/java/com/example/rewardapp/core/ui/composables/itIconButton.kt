package com.example.rewardapp.core.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rewardapp.RoundedCornerDimension
import com.example.rewardapp.core.ui.theme.RewardAppTheme

@Composable
fun ITiconButton(

    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit

) {
    val iconButtonBackground = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(RoundedCornerDimension))
            .background(iconButtonBackground)
    ) {
        content()
    }
}

@Preview(
    name = " eward item Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = " Reward item Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
private fun ScreenContentPreview() {
    RewardAppTheme {
        Surface {
            Box(modifier = Modifier.padding(64.dp), Alignment.Center) {
                ITiconButton(
                    modifier = Modifier,
                    onClick = {}
                ) {
                    Icon(Icons.Default.Star, contentDescription = null)
                }
            }
        }
    }
}
