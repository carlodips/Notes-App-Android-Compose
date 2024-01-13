package dev.carlodips.notes_compose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
    onClick: () -> Unit = {},
    isEnabled: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                onClick = { onClick.invoke() },
                enabled = isEnabled
            ),
        shape = shape,
        content = content
    )

    //border = BorderStroke(2.dp, MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high))
}
