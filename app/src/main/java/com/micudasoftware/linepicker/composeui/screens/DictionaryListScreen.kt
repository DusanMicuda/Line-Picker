package com.micudasoftware.linepicker.composeui.screens

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.composeui.screens.destinations.DictionaryScreenDestination
import com.micudasoftware.linepicker.composeui.viewmodels.DictionaryListViewModel
import com.micudasoftware.linepicker.db.DictionaryInfo
import com.micudasoftware.linepicker.other.Constants
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun DictionaryListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: DictionaryListViewModel = hiltViewModel()
) {
    val dictionaryList by viewModel.dictionaryList.collectAsState(initial = emptyList())

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val uri = result.data?.data
            if (uri != null)
                viewModel.getDictionaryFromFile(uri)
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

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val backPressedCallback = remember{
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.showDeleteLayout(false)
            }

        }
    }

    LaunchedEffect(key1 = viewModel.deleteLayoutIsVisible) {
        if (viewModel.deleteLayoutIsVisible)
            backPressedDispatcher?.addCallback(backPressedCallback)
        else
            backPressedCallback.remove()
    }
    DisposableEffect(key1 = LocalLifecycleOwner.current) {
        onDispose { backPressedCallback.remove() }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            if (!viewModel.deleteLayoutIsVisible) {
                FloatingActionButton(
                    onClick = { launcher.launch(intent) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = { viewModel.removeDictionaries() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    ) {
        Background(dictionaryListIsEmpty = dictionaryList.isEmpty())
        ListOfDictionaries(navigator = navigator, dictionaryList = dictionaryList)
        AppBar(navigator = navigator)
        if (viewModel.editDialogIsVisible)
            EditDialog()
    }
}

@Composable
fun ListOfDictionaries(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    dictionaryList: List<DictionaryInfo>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 44.dp),
        contentPadding = PaddingValues(top = 46.dp, bottom = 74.dp),
        content = {
            items(dictionaryList) { dictionary ->
                ListOfDictionariesItem(
                    dictionary = dictionary,
                    navigator = navigator
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListOfDictionariesItem(
    modifier: Modifier = Modifier,
    dictionary: DictionaryInfo,
    navigator: DestinationsNavigator,
    viewModel: DictionaryListViewModel = hiltViewModel(),
) {
    var checkBoxState by remember { mutableStateOf(false)}
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .combinedClickable(
                onClick = {
                    navigator.navigate(DictionaryScreenDestination(dictionary.id))
                },
                onLongClick = {
                    viewModel.showDeleteLayout(true)
                    checkBoxState = true
                    viewModel.checkedDictionaries.add(dictionary)
                }
            ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column {
                Text(
                    text = dictionary.name,
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = dictionary.description,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            if (viewModel.deleteLayoutIsVisible) {
                Checkbox(
                    checked = checkBoxState,
                    onCheckedChange = { isChecked ->
                        checkBoxState = isChecked
                        if (isChecked)
                            viewModel.checkedDictionaries.add(dictionary)
                        else
                            viewModel.checkedDictionaries.remove(dictionary)
                    }
                )
            } else {
                IconButton(onClick = { viewModel.editDictionary(dictionary) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                }
            }
        }
    }
}

@Composable
fun Background(
    modifier: Modifier = Modifier,
    dictionaryListIsEmpty: Boolean
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(top = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            var text by remember {mutableStateOf("Please import a file")}
            text = if (dictionaryListIsEmpty) {
                "Please import a file"
            } else {
                ""
            }

            Text(
                modifier = Modifier.padding(16.dp),
                text = text,
                style = MaterialTheme.typography.subtitle1
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                painter = painterResource(id = R.drawable.ic_backgound),
                alpha = 0.5f,
                contentDescription = "background"
            )
        }
    }
}

@Composable
fun EditDialog(
    viewModel: DictionaryListViewModel = hiltViewModel()
) {
    var name by remember{ mutableStateOf("") }
    var description by remember{ mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    name = viewModel.dictionary.name
    if (viewModel.dictionary.assignment != null)
        description = viewModel.dictionary.assignment!!

    Dialog(
        onDismissRequest = { viewModel.cancelEdit() }
    ) {
        Card(
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Import file",
                    style = MaterialTheme.typography.h2
                )
                OutlinedTextField(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    value = name,
                    label = { Text(text = "Name") },
                    onValueChange = { name = it }
                )
                OutlinedTextField(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    value = description,
                    label = { Text(text = "Description") },
                    onValueChange = { description = it }
                )
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        text = errorMessage,
                        style = MaterialTheme.typography.h6
                    )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                    ) {
                    OutlinedButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        shape = MaterialTheme.shapes.large,
                        onClick = { viewModel.cancelEdit() }
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        shape = MaterialTheme.shapes.large,
                        onClick = {
                            if (name.isNotEmpty() && description.isNotEmpty()) {
                                viewModel.insertDictionary(name, description)
                            } else {
                                errorMessage = when{
                                    description.isEmpty() ->
                                        "Description can`t be empty!"
                                    name.isEmpty() ->
                                        "Name can`t be empty!"
                                    else -> ""
                                }
                            }
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}