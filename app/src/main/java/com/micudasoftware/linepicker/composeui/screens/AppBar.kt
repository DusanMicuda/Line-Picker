package com.micudasoftware.linepicker.composeui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.composeui.viewmodels.AppBarViewModel
import com.micudasoftware.linepicker.composeui.viewmodels.DictionaryListViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    onRemoveButtonClick: () -> Unit = {},
    viewModel: AppBarViewModel = hiltViewModel()
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(text = stringResource(id = R.string.app_name))
        if (viewModel.removeButtonIsVisible)
            RemoveButton(onRemoveButtonClick = onRemoveButtonClick)
        else
            MenuButton(navigator = navigator)
    }
}

@Composable
fun MenuButton(
    navigator: DestinationsNavigator
) {
    var expanded by remember { mutableStateOf(false) }
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
    viewModel: DictionaryListViewModel = hiltViewModel(),
    onRemoveButtonClick: () -> Unit
) {
    IconButton(
        onClick = onRemoveButtonClick
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
    }
}