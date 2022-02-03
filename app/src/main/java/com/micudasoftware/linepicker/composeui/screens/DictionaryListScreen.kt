package com.micudasoftware.linepicker.composeui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.micudasoftware.linepicker.other.Constants
import com.micudasoftware.linepicker.other.Event
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun DictionaryListScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: DictionaryListViewModel = hiltViewModel()
) {
    val dictionaryList by viewModel.dictionaryList.collectAsState(initial = emptyList())
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val uri = result.data?.data
            uri.let { viewModel.onEvent(Event.OnGetFile(it!!)) }
        }
    )
    val intent =  remember {
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf(
                    Constants.FILE_TYPE_XLS,
                    Constants.FILE_TYPE_XLSX
                ))
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { launcher.launch(intent) }
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
            DictionaryList(navigator = navigator, dictionaryList = dictionaryList)
        }
    }
}

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    viewModel: DictionaryListViewModel = hiltViewModel()
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(text = stringResource(id = R.string.app_name))
        if (viewModel.removeLayoutIsVisible)
            RemoveButton()
        else
            MenuButton()
    }
}

@Composable
fun MenuButton() {
    var expanded by remember { mutableStateOf(false)}
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

@Composable
fun RemoveButton(
    viewModel: DictionaryListViewModel = hiltViewModel()
) {
    IconButton(
        onClick = { viewModel.onEvent(Event.OnRemoveDictionaries) }
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
    }
}

@Composable
fun DictionaryList(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    dictionaryList: List<Dictionary>
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            items(dictionaryList) { dictionary ->
                DictionaryListItem(
                    dictionary = dictionary,
                    navigator = navigator
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryListItem(
    modifier: Modifier = Modifier,
    dictionary: Dictionary,
    navigator: DestinationsNavigator,
    viewModel: DictionaryListViewModel = hiltViewModel()
) {
    val checkBoxState by remember { mutableStateOf(false)}
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    //TODO onItemClick
                },
                onLongClick = {
                    viewModel.removeLayoutIsVisible = true
                }
            ),
        shape = RoundedCornerShape(8.dp),
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
            if (viewModel.removeLayoutIsVisible) {
                Checkbox(
                    checked = checkBoxState,
                    onCheckedChange = { isChecked ->
                        if (isChecked)
                            viewModel.checkedDictionaries.add(dictionary)
                        else
                            viewModel.checkedDictionaries.remove(dictionary)
                    }
                )
            }
        }
    }
}