package com.vkolisnichenko.slarket.android.ui


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vkolisnichenko.slarket.android.R
import com.vkolisnichenko.slarket.domain.model.BasketItemModel
import com.vkolisnichenko.slarket.domain.model.PizzaModel
import com.vkolisnichenko.slarket.domain.model.RestaurantDetailsModel
import com.vkolisnichenko.slarket.presentation.details.RestDetailsStateError
import com.vkolisnichenko.slarket.presentation.details.RestDetailsStateLoading
import com.vkolisnichenko.slarket.presentation.details.RestDetailsStateSuccess
import com.vkolisnichenko.slarket.presentation.details.RestaurantDetailsViewModel
import com.vkolisnichenko.slarket.resources.SharedResources
import kotlinx.coroutines.launch

@Composable
fun RestaurantDetailsScreen(
    viewModel: RestaurantDetailsViewModel,
    onBackClick: () -> Unit,
    id: Int,
    onBasketClick: () -> Unit
) {
    val state by viewModel.restDetailsState.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchRestDetails(id)
    }

    when (state) {
        is RestDetailsStateLoading -> LoadingScreen()
        is RestDetailsStateError -> ErrorScreen((state as RestDetailsStateError).message)
        is RestDetailsStateSuccess -> RestaurantDetailsContent(
            details = (state as RestDetailsStateSuccess).restaurantDetailsModel,
            onBackClick = onBackClick,
            viewModel = viewModel,
            onBasketClick = onBasketClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    onBackClick: () -> Unit,
    restName: String
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back button",
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = restName)
            }
        }
    )
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailsContent(
    details: RestaurantDetailsModel,
    onBackClick: () -> Unit,
    viewModel: RestaurantDetailsViewModel,
    onBasketClick: () -> Unit
) {
    val isFavorite by rememberUpdatedState(details.isFavourite)
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var lastSelectedPizza: PizzaModel by remember { mutableStateOf(PizzaModel()) }

    LaunchedEffect(key1 = Unit) {
        viewModel.isShowBasketFAB()
    }

    val hideBottomSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            showBottomSheet = false
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        ReplaceBasketDialog(
            showDialog = details.displayBasketDialog,
            onDismiss = { details.displayBasketDialog = false },
            onConfirm = { viewModel.replaceBasketItem() }
        )

        Column {
            AppBar(onBackClick = onBackClick, restName = details.restName)
            LazyColumn {
                item {
                    AsyncImage(
                        model = details.headerImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = details.restName,
                                style = MaterialTheme.typography.displaySmall,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            )
                            Text(
                                text = details.restDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                if (!isFavorite) {
                                    viewModel.addToFavourite()
                                } else {
                                    viewModel.removeFromFavourite()
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                                tint = if (isFavorite) Color.Red else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                items(details.pizzaList.size) { index ->
                    PizzaItem(pizza = details.pizzaList[index], bottomSheetCallback = { id ->
                        lastSelectedPizza = details.pizzaList.first { it.id == id }
                        showBottomSheet = true
                    })
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { hideBottomSheet.invoke() },
                    modifier = Modifier.navigationBarsPadding(),
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                ) {

                    LaunchedEffect(key1 = lastSelectedPizza) {
                        viewModel.resetBasketModel(lastSelectedPizza)
                    }

                    lastSelectedPizza.let {
                        PizzaBottomSheetContent(
                            pizza = lastSelectedPizza,
                            onAddToBasketClick = {
                                viewModel.addToBasket()
                                hideBottomSheet.invoke()
                            },
                            basketItemModel = details.basketItemModel,
                            onDishSelected = { isSelected ->
                                viewModel.updateSelectedDish(isSelected)
                            }
                        )
                    }
                }
            }
        }
        if (details.showBasket) {
            FloatingActionButton(
                onClick = onBasketClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(18.dp),
                containerColor = colorResource(id = R.color.orange_price),
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Basket"
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaItem(pizza: PizzaModel, bottomSheetCallback: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.light_silver),
        ),
        onClick = { bottomSheetCallback.invoke(pizza.id) }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = pizza.pizzaImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = pizza.pizzaName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${pizza.pizzaPrice}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(id = R.color.orange_price),
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = pizza.desc,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 4.dp)
                )
            }

        }
    }
}

@Composable
fun PizzaBottomSheetContent(
    pizza: PizzaModel,
    onAddToBasketClick: () -> Unit,
    basketItemModel: BasketItemModel,
    onDishSelected: (isSelected: Boolean) -> Unit
) {
    var userNotes by remember { mutableStateOf(TextFieldValue()) }

    basketItemModel.userWishes = userNotes.text
    basketItemModel.dishImage = pizza.pizzaImageUrl

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .padding(bottom = 50.dp)
    ) {
        AsyncImage(
            model = basketItemModel.dishImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(
                text = basketItemModel.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f),
                textAlign = TextAlign.Start,
            )

            Text(
                text = "${basketItemModel.sum}$",
                style = MaterialTheme.typography.headlineMedium,
                color = colorResource(id = R.color.orange_price),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        Text(
            text = "Ingredients: ${pizza.desc}",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        CutleryAndSauces(basketItemModel, onDishSelected)

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = userNotes,
            onValueChange = { userNotes = it },
            label = { Text(stringResource(id = SharedResources.strings.feel_free_to_write_your_wishes.resourceId)) },
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 5,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = colorResource(id = R.color.silver),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )

        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddToBasketClick,
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange_price)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = stringResource(id = SharedResources.strings.add_to_basket.resourceId), style = MaterialTheme.typography.bodyMedium)
        }

    }
}

@Composable
fun CutleryAndSauces(
    basketItemModel: BasketItemModel,
    onDishSelected: (isSelected: Boolean) -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    Column {
        AddonItem(AddonsType.CUTLERY, basketItemModel, onDishSelected)
        AddonItem(AddonsType.SAUCES, basketItemModel, onDishSelected)
    }
}

@Composable
fun AddonItem(
    type: AddonsType,
    basketItemModel: BasketItemModel,
    onDishSelected: (isSelected: Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }
    val text = if (type == AddonsType.SAUCES) {
        "I want to add sauces (1$)"
    } else {
        "I want to add cutlery (1$)"
    }

    if (type == AddonsType.SAUCES) basketItemModel.isSauces = isChecked
    else basketItemModel.isCutlery = isChecked

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge
        )
        Switch(
            checked = isChecked, onCheckedChange = {
                isChecked = isChecked.not()
                onDishSelected.invoke(isChecked)
            }, colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(id = R.color.white),
                checkedTrackColor = colorResource(id = R.color.orange_price)
            )
        )
    }
}


@Composable
fun ReplaceBasketDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = stringResource(id = SharedResources.strings.replace_items_in_basket.resourceId))
            },
            text = {
                Text(text = stringResource(id = SharedResources.strings.you_already_have_items_in_your_basket.resourceId))
            },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("No")
                }
            }
        )
    }
}
