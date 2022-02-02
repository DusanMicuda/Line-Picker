package com.micudasoftware.linepicker.composeui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.composeui.viewmodels.DictionaryListViewModel
import com.micudasoftware.linepicker.db.Dictionary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun DictionaryListScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: DictionaryListViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /*TODO add new dictionary*/
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppBar()
            DictionaryList(navigator = navigator, dictionaryList = viewModel.dictionaryList)
        }
    }
}

@Composable
fun AppBar(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false)}
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(text = stringResource(id = R.string.app_name))
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = { /*TODO navigate to pdf*/ }) {
                    Text(text = stringResource(id = R.string.menu_user_manual))
                }
                DropdownMenuItem(onClick = { /*TODO navigate to pdf*/ }) {
                    Text(text = stringResource(id = R.string.menu_license_agreement))
                }
                DropdownMenuItem(onClick = { /*TODO navigate to pdf*/ }) {
                    Text(text = stringResource(id = R.string.menu_software_version))
                }
                DropdownMenuItem(onClick = { /*TODO navigate to pdf*/ }) {
                    Text(text = stringResource(id = R.string.menu_about_us))
                }
            }
        }
    }
}

@Composable
fun DictionaryList(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    dictionaryList: List<Dictionary>
) {
    val checkBoxIsVisible = remember { mutableStateOf(false) }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            items(dictionaryList) { dictionary ->
                DictionaryListItem(
                    dictionary = dictionary,
                    checkBoxIsVisible = checkBoxIsVisible
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DictionaryListItem(
    modifier: Modifier = Modifier,
    dictionary: Dictionary,
    navigator: DestinationsNavigator,
    checkBoxIsVisible: MutableState<Boolean>
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { /*TODO navigate to DictionaryScreen*/ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column {
                Text(text = dictionary.name)
                dictionary.assignment?.let { Text(text = it) }
            }
            if (checkBoxIsVisible.value) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {/*TODO onCheckedChange*/ }
                )
            }
        }
    }
}