package com.micudasoftware.linepicker.composeui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.composeui.theme.AppBarShape
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = MaterialTheme.colors.primaryVariant,
        darkIcons = false
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary, AppBarShape()),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 24.dp),
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h1
        )
        MenuButton(
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 20.dp, end = 8.dp),
            navigator = navigator
        )
    }
}

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
        modifier = modifier,
        onClick = { expanded = true }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = "menu"
        )
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