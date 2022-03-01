package com.micudasoftware.linepicker.composeui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.micudasoftware.linepicker.R
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

    if (viewModel.randomizeDialogState)
        RandomizeDialog()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.showRandomizeDialog() }) {
                Icon(painter = painterResource(id = R.drawable.outline_shuffle_24), contentDescription = "randomize")
            }
        }
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            AppBar(navigator = navigator)
            if (dictionaryIsNotNull) {
                Assignment(dictionary = viewModel.dictionary.value)
                Header(dictionary = viewModel.dictionary.value)
                DictionaryList(dictionary = viewModel.dictionary.value)
            }
        }
    }
}

@Composable
fun Assignment(
    modifier: Modifier = Modifier,
    dictionary: Dictionary
) {
    dictionary.assignment?.let {
        Row(
            modifier = modifier
                .padding(8.dp)
        ) {
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
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(row.isNotEmpty()) {
                Text(
                    modifier = modifier.fillMaxWidth(0.33f),
                    text = row[0]
                )
                Text(
                    modifier = modifier.fillMaxWidth(0.5f),
                    text = row[1]
                )
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = row[2]
                )
            }
        }
    }
}

@Composable
fun RandomizeDialog(
    viewModel: DictionaryViewModel = hiltViewModel()
) {
    var errorMessage by remember{ mutableStateOf("") }
    var enteredCount by remember{ mutableStateOf("") }

    Dialog(
        onDismissRequest = { viewModel.cancelRandomize() }
    ) {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colors.surface,
                    MaterialTheme.shapes.medium
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Randomize",
                style = MaterialTheme.typography.h2
            )
            OutlinedTextField(
                modifier = Modifier.padding(8.dp),
                value = enteredCount,
                label = { Text(text = "Count: 1 - ${viewModel.dictionary.value.dictionary.size}") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { enteredCount = it }
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .align(Alignment.Start),
                text = errorMessage,
                style = MaterialTheme.typography.h6
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(4.dp),
                    shape = MaterialTheme.shapes.large,
                    onClick = { viewModel.cancelRandomize() }
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    modifier = Modifier.padding(4.dp),
                    shape = MaterialTheme.shapes.large,
                    onClick = {
                        if (enteredCount.isNotEmpty() && enteredCount.isDigitsOnly()) {
                            viewModel.randomize(enteredCount.toInt())
                        } else {
                            errorMessage = "Wrong Count!"
                        }
                    }
                ) {
                    Text(text = "Randomize")
                }
            }
        }
    }
}