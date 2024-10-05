package com.vkolisnichenko.slarket.android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vkolisnichenko.slarket.resources.SharedResources
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    content: @Composable (drawerState: DrawerState) -> Unit,
    navHostController: NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("More", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = SharedResources.strings.favourites.resourceId)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favourites Icon"
                        )
                    },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate("${Screens.RESTAURANTS.route}/true")
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = SharedResources.strings.recent_orders.resourceId)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Recent Orders Icon"
                        )
                    },
                    selected = false,
                    onClick = { }
                )
                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = SharedResources.strings.basket.resourceId)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Basket Icon"
                        )
                    },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate(Screens.BASKET.route)
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Support") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Support Icon"
                        )
                    },
                    selected = false,
                    onClick = { }
                )
            }
        },
    ) {
        content.invoke(drawerState)
    }
}