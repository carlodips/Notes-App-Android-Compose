package dev.carlodips.notes_compose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.carlodips.notes_compose.R

@Composable
fun BaseDialog(
    setShowDialog: (Boolean) -> Unit,
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
    title: String,
    message: String,
    onPositiveText: String = stringResource(R.string.yes),
    onNegativeText: String = stringResource(R.string.no),
) {

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface {
            Box(contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 12.dp),
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onNegativeClick.invoke()
                                    setShowDialog(false)
                                },
                            text = onNegativeText,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onPositiveClick.invoke()
                                    setShowDialog(false)
                                },
                            text = onPositiveText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}