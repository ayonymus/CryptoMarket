package com.ayon.cryptomarket.ui.market

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterView(
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>,
    placeHolder: String = "Filter"
) {

    TextField(
        modifier = modifier,
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        placeholder = { Text(text = placeHolder) },
        label = { Text(text = placeHolder) }
    )
}


@Preview
@Composable
fun FilterViewPreview() {
    FilterView(state = mutableStateOf(TextFieldValue()), placeHolder = "Filter items")
}

@Preview
@Composable
fun FitlerViewPreview2() {
    FilterView(state = mutableStateOf(TextFieldValue("Thing")), placeHolder = "Filter items")
}