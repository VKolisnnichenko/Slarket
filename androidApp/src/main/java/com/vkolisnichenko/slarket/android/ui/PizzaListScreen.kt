package com.vkolisnichenko.slarket.android.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.vkolisnichenko.slarket.android.MyApplicationTheme
import com.vkolisnichenko.slarket.android.R
import com.vkolisnichenko.slarket.domain.model.PizzaModel
import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel
import com.vkolisnichenko.slarket.presentation.rests.PizzaListEmpty
import com.vkolisnichenko.slarket.presentation.rests.PizzaListError
import com.vkolisnichenko.slarket.presentation.rests.PizzaListLoading
import com.vkolisnichenko.slarket.presentation.rests.PizzaListSuccess
import com.vkolisnichenko.slarket.presentation.rests.PizzaListViewModel
import com.vkolisnichenko.slarket.resources.SharedResources
import kotlinx.coroutines.launch

@Composable
fun PizzaListScreen(
    onAboutButtonClick: () -> Unit,
    onRestClick: (id: Int) -> Unit,
    pizzaListViewModel: PizzaListViewModel,
    drawerState: DrawerState,
    isOnlyFavourites: Boolean = false,
    onBackClick: () -> Unit
) {
    val articlesState = pizzaListViewModel.pizzaListState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        if (isOnlyFavourites) {
            pizzaListViewModel.fetchFavouriteRests()
        } else {
            pizzaListViewModel.fetchData()
        }
    }

    Column {
        AppBar(onAboutButtonClick, drawerState, isOnlyFavourites, onBackClick)
        if (articlesState.value is PizzaListLoading)
            LoadingState()
        if (articlesState.value is PizzaListError)
            ErrorMessage((articlesState.value as PizzaListError).message)
        if (articlesState.value is PizzaListSuccess)
            ArticlesListView(pizzaListViewModel, onRestClick, isOnlyFavourites)
        if (articlesState.value is PizzaListEmpty)
            EmptyFavouritesScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    onAboutButtonClick: () -> Unit,
    drawerState: DrawerState,
    isFavourites: Boolean = false,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (!isFavourites) {
                        drawerState.apply {
                            scope.launch {
                                if (isClosed) open() else close()
                            }

                        }
                    } else {
                        onBackClick.invoke()
                    }
                }) {
                    Icon(
                        imageVector = if (!isFavourites) Icons.Outlined.List else Icons.Filled.ArrowBack,
                        contentDescription = "Sources Button",
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = if (!isFavourites) SharedResources.strings.restaurants.resourceId else SharedResources.strings.favourites.resourceId))
            }
        },
        actions = {
            IconButton(onClick = onAboutButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "About Device Button",
                )
            }
        }
    )
}


@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp))
    }
}


@Composable
fun ArticlesListView(
    viewModel: PizzaListViewModel,
    onRestClick: (id: Int) -> Unit,
    isFavourite: Boolean = false
) {

    val scope = rememberCoroutineScope()
    SwipeRefresh(
        state = SwipeRefreshState(viewModel.pizzaListState.value is PizzaListLoading),
        onRefresh = {
            scope.launch {
                if (!isFavourite) {
                    viewModel.fetchData()
                } else {
                    viewModel.fetchFavouriteRests()
                }
            }
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (viewModel.pizzaListState.value is PizzaListSuccess) {
                items((viewModel.pizzaListState.value as PizzaListSuccess).items) { pizzaItem ->
                    PizzaItemView(
                        pizzaItem = pizzaItem,
                        onRestClick = onRestClick,
                        onFavouriteClick = { isFavourite ->
                            if (isFavourite) {
                                viewModel.deleteFavouriteRestaurant(pizzaItem.id.toLong())
                            } else {
                                viewModel.insertFavouriteRestaurant(pizzaItem)
                            }
                        })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaItemView(
    pizzaItem: PizzaRestaurantItemModel,
    onRestClick: (id: Int) -> Unit,
    onFavouriteClick: (isFavourite: Boolean) -> Unit
) {
    val isFavorite by rememberUpdatedState(pizzaItem.isFavourite)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onRestClick.invoke(pizzaItem.id)
        },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.light_silver),
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = pizzaItem.image,
                contentDescription = pizzaItem.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = pizzaItem.title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    onFavouriteClick.invoke(isFavorite)
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            if (pizzaItem.messageDescription.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pizzaItem.messageDescription,
                    style = TextStyle(color = Color.Gray),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EmptyFavouritesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .padding(bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = SharedResources.images.empty_list.drawableResId,
                    contentDescription = "Empty basket image",
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "You don't have any favorite restaurants yet",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center)
        )
    }
}


@Preview
@Composable
fun ArticlesScreenPreview() {
    MyApplicationTheme {

    }
}