package com.ayon.cryptomarket.ui.market

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.ayon.cryptomarket.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterView(
    modifier: Modifier = Modifier,
    value: String,
    onFilterChange: (text: String) -> Unit,
    placeHolder: String = stringResource(id = R.string.filter)
) {

    TextField(
        modifier = modifier,
        value =value,
        onValueChange = { newValue ->
            onFilterChange(newValue)
        },
        placeholder = { Text(text = placeHolder) },
        label = { Text(text = placeHolder) }
    )
}


@Preview
@Composable
fun FilterViewPreview() {
    FilterView(value="Thing", onFilterChange = { })
}

@Preview
@Composable
fun FilterViewPreview2() {
   FilterView(value="Thing", onFilterChange = { })
}