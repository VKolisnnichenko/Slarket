package com.vkolisnichenko.slarket.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.vkolisnichenko.slarket.android.ui.AppScaffold
import com.vkolisnichenko.slarket.presentation.basket.BasketViewModel
import com.vkolisnichenko.slarket.presentation.details.RestaurantDetailsViewModel
import com.vkolisnichenko.slarket.presentation.rests.PizzaListViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pizzaListViewModel: PizzaListViewModel by inject()
        val restaurantDetailsViewModel: RestaurantDetailsViewModel by inject()
        val basketViewModel : BasketViewModel by inject()

        setContent {
            val navController = rememberNavController()

                MyApplicationTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        AppScaffold(
                            pizzaListViewModel = pizzaListViewModel,
                            restaurantDetailsViewModel = restaurantDetailsViewModel,
                            basketViewModel = basketViewModel,
                            navController = navController
                        )
                    }
                }
        }
    }
}


@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {

    }
}
