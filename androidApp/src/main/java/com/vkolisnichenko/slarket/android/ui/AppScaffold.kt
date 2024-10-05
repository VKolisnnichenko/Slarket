package com.vkolisnichenko.slarket.android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vkolisnichenko.slarket.presentation.basket.BasketViewModel
import com.vkolisnichenko.slarket.presentation.details.RestaurantDetailsViewModel
import com.vkolisnichenko.slarket.presentation.rests.PizzaListViewModel

@Composable
fun AppScaffold(
    pizzaListViewModel: PizzaListViewModel,
    restaurantDetailsViewModel: RestaurantDetailsViewModel,
    basketViewModel: BasketViewModel,
    navController: NavHostController
) {

    Scaffold {
        AppNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            pizzaListViewModel = pizzaListViewModel,
            restaurantDetailsViewModel = restaurantDetailsViewModel,
            basketViewModel = basketViewModel
        )
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    pizzaListViewModel: PizzaListViewModel,
    restaurantDetailsViewModel: RestaurantDetailsViewModel,
    basketViewModel: BasketViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "${Screens.RESTAURANTS.route}/false",
        modifier = modifier,
    ) {
        composable(
            route = "${Screens.RESTAURANTS.route}/{isFavourites}",
            arguments = listOf(navArgument("isFavourites") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isFavourites = backStackEntry.arguments?.getBoolean("isFavourites") ?: false

            NavigationDrawer({
                PizzaListScreen(
                    onAboutButtonClick = { navController.navigate(Screens.ABOUT_DEVICE.route) },
                    onRestClick = { id ->
                        navController.navigate("${Screens.REST_DETAILS_SCREEN.route}/$id")
                    },
                    pizzaListViewModel,
                    it,
                    isOnlyFavourites = isFavourites,
                    onBackClick = {
                        navController.popBackStackOrIgnore()
                    }
                )
            }, navController)
        }

        composable(Screens.ABOUT_DEVICE.route) {
            AboutScreen(backClickCallback = { navController.popBackStack() })
        }

        composable(
            route = "${Screens.REST_DETAILS_SCREEN.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id")
            id?.let {
                RestaurantDetailsScreen(
                    restaurantDetailsViewModel,
                    { navController.popBackStack() },
                    id,
                    {
                        navController.navigate(Screens.BASKET.route)
                    })
            }
        }
        composable(Screens.BASKET.route) {
            BasketScreen(basketViewModel) {
                navController.popBackStackOrIgnore()
            }
        }
    }
}

fun NavController.popBackStackOrIgnore() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}
