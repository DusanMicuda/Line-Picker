package com.micudasoftware.linepicker.composeui.screens

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
    modifier: Modifier = Modifier,
    navigator : DestinationsNavigator,
    dictionary: Dictionary,
    viewModel: DictionaryViewModel = hiltViewModel()
) {
    val enteredCount by remember{ mutableStateOf("") }
    var rememberDictionary by remember{ mutableStateOf(dictionary) }
    var errorIsVisible by remember{ mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        AppBar(navigator = navigator)
        DictionaryList(dictionary = dictionary)
    }
    Row(
        modifier = Modifier.fillMaxSize(),
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
                        label = { Text(text = "Count: 1 - ${dictionary.dictionary.size}")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { errorIsVisible = false }
                    )
                    Button(
                        onClick = {
                            if (enteredCount.isNotEmpty() &&
                                enteredCount.isDigitsOnly()) {
                                if (enteredCount.toInt() > 0 &&
                                    enteredCount.toInt() < dictionary.dictionary.size) {
                                    rememberDictionary = viewModel.randomize(rememberDictionary, enteredCount.toInt())
                                } else
                                    errorIsVisible = true
                            } else
                                errorIsVisible = true
                        }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryList(
    modifier: Modifier = Modifier,
    dictionary: Dictionary
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            dictionary.assignment.let {
                stickyHeader {
                    Text(text = it!!)
                }
            }

            if (dictionary.headerColumn1 != null &&
                dictionary.headerColumn2 != null &&
                dictionary.headerColumn3 != null) {
                stickyHeader {
                    DictionaryListItem(row = mutableListOf(dictionary.headerColumn1,dictionary.headerColumn2, dictionary.headerColumn3))
                }
            }

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
            Text(text = row[0])
            Text(text = row[1])
            Text(text = row[2])
        }
    }
}