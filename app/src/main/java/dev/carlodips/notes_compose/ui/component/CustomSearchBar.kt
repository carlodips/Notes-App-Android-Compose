package dev.carlodips.notes_compose.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.ui.theme.NotesComposeTheme
import dev.carlodips.notes_compose.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(SearchBarDefaults.InputFieldHeight),
        value = query,
        onValueChange = {
            onQueryChange.invoke(it)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        textStyle = LocalTextStyle.current.copy(
            color = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            }
        ),
        cursorBrush = SolidColor(Purple80),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = placeholder,
                leadingIcon = leadingIcon?.let { leading ->
                    {
                        Box(Modifier.offset(x = 4.dp)) { leading() }
                    }
                },
                trailingIcon = trailingIcon?.let { trailing ->
                    {
                        Box(Modifier.offset(x = (-4).dp)) { trailing() }
                    }
                },
                shape = SearchBarDefaults.inputFieldShape,
                //colors = colors,
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                container = {},
            )
        }
    )
}

@Preview
@Composable
fun PreviewCustomSearchBar() {
    NotesComposeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                CustomSearchBar(
                    query = "",
                    leadingIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.placeholder_search)
                        )
                    }
                )

                Text("Preview of Search bar")
            }
        }
    }
}