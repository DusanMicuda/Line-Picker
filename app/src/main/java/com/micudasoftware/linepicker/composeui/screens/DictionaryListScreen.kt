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
            uri.let { viewModel.getFile(it!!) }
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
        Image(
            modifier = Modifier.fillMaxHeight(0.85f),
            painter = painterResource(id = R.drawable.ic_backgound),
            alignment = Alignment.BottomCenter,
            alpha = 0.5f,
            contentDescription = "background"
        )
        ListOfDictionaries(navigator = navigator, dictionaryList = dictionaryList)
        AppBar(navigator = navigator)
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
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = dictionary.assignment,
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
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                }
            }
        }
    }
}