package com.micudasoftware.linepicker.composeui.screens

import android.media.midi.MidiOutputPort
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.micudasoftware.linepicker.composeui.viewmodels.DictionaryViewModel
import com.micudasoftware.linepicker.db.Dictionary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun DictionaryScreen(
    navigator : DestinationsNavigator,
    modifier: Modifier = Modifier,
    dictionaryId: Int,
    viewModel: DictionaryViewModel = hiltViewModel()
) {
    var dictionaryIsNotNull by remember{ mutableStateOf(false) }

    viewModel.dictionary = viewModel.getDictionary(dictionaryId).collectAsState(initial = Dictionary.empty())
    LaunchedEffect(key1 = viewModel.dictionary, block = {
        dictionaryIsNotNull = true
    })

    Column(modifier = modifier.fillMaxSize()) {
        AppBar(navigator = navigator)
        if (dictionaryIsNotNull) {
            Assignment(dictionary = viewModel.dictionary.value)
            Header(dictionary = viewModel.dictionary.value)
            DictionaryList(dictionary = viewModel.dictionary.value)
        }
    }
    BottomBar(dictionaryIsNotNull = dictionaryIsNotNull)
}

@Composable
fun Assignment(
    modifier: Modifier = Modifier,
    dictionary: Dictionary
) {
    dictionary.assignment?.let {
        Row(modifier = modifier) {
            Text(text = it)
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    dictionary: Dictionary
) {
    if (dictionary.headerColumn1 != null &&
        dictionary.headerColumn2 != null &&
        dictionary.headerColumn3 != null) {
            DictionaryListItem(
                modifier = modifier,
                row = mutableListOf(
                    dictionary.headerColumn1,
                    dictionary.headerColumn2,
                    dictionary.headerColumn3)
            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryList(
    modifier: Modifier = Modifier,
    dictionary: Dictionary
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            items(dictionary.dictionary) { row ->
                DictionaryListItem(row = row)
            }
        }
    )
}

@Composable
fun DictionaryListItem(
    modifier: Modifier = Modifier,
    row: List<String>
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(row.isNotEmpty()) {
                Text(text = row[0])
                Text(text = row[1])
                Text(text = row[2])
            }
        }
    }
}

@Composable
fun BottomBar(
    modifier : Modifier = Modifier,
    dictionaryIsNotNull: Boolean,
    viewModel: DictionaryViewModel = hiltViewModel()
) {
    val enteredCount by remember{ mutableStateOf(TextFieldValue("")) }
    var errorIsVisible by remember{ mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = AbsoluteRoundedCornerShape(topLeft = 10.dp, topRight = 10.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = enteredCount,
                        label = {
                            if (dictionaryIsNotNull)
                                Text(text = "Count: 1 - ${viewModel.dictionary.value.dictionary.size}")
                        },
                        enabled = dictionaryIsNotNull,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { errorIsVisible = false }
                    )
                    Button(
                        onClick = {
                            if (enteredCount.text.isNotEmpty() &&
                                enteredCount.text.isDigitsOnly()) {
                                if (enteredCount.text.toInt() > 0 &&
                                    enteredCount.text.toInt() < viewModel.dictionary.value.dictionary.size) {
                                    viewModel.randomize(enteredCount.text.toInt())
                                } else
                                    errorIsVisible = true
                            } else
                                errorIsVisible = true
                        },
                        enabled = dictionaryIsNotNull
                    ) {
                        Text(text = "Randomize")
                    }
                }
                if (errorIsVisible)
                    Text(text = "Wrong Count!")
            }

        }
    }
}