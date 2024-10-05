package com.vkolisnichenko.slarket.android.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vkolisnichenko.slarket.android.R
import com.vkolisnichenko.slarket.data.mockDataPizzaList
import com.vkolisnichenko.slarket.domain.model.BasketItemModel
import com.vkolisnichenko.slarket.domain.model.BasketPresentationModel
import com.vkolisnichenko.slarket.presentation.basket.BasketStateEmpty
import com.vkolisnichenko.slarket.presentation.basket.BasketStateError
import com.vkolisnichenko.slarket.presentation.basket.BasketStateLoading
import com.vkolisnichenko.slarket.presentation.basket.BasketStateSuccess
import com.vkolisnichenko.slarket.presentation.basket.BasketViewModel
import com.vkolisnichenko.slarket.resources.SharedResources


@Composable
fun BasketScreen(
    basketViewModel: BasketViewModel,
    onBackClick: () -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        basketViewModel.fetchBasketItems()
    }
    val state by basketViewModel.basketState.collectAsState()


    when (state) {
        is BasketStateLoading -> BasketLoading()
        is BasketStateError -> BasketError((state as BasketStateError).message)
        is BasketStateSuccess -> BasketSuccessScreen((state as BasketStateSuccess).basketPresentationModel, onBackClick, {}, basketViewModel::deleteBasketItem)
        is BasketStateEmpty -> EmptyBasketScreen(onBackClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketSuccessScreen(
    basketPresentationModel: BasketPresentationModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onDeleteBasketItemById: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
                    Text(text = stringResource(id = SharedResources.strings.basket.resourceId))
                }
            }
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            item {
                TitleText(
                    restName = mockDataPizzaList.first { it.id == basketPresentationModel.basketItems.first().restId }.title,
                    onRestNameClick = { }
                )
            }

            items(basketPresentationModel.basketItems) { item ->
                BasketItemCard(basketItem = item) { onDeleteBasketItemById(item.id) }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total:",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "$${basketPresentationModel.totalCost}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onContinueClick,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp),
        ) {
            Text(text = "Continue", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyBasketScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    Text(text = stringResource(id = SharedResources.strings.basket.resourceId))
                }
            }
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.padding(bottom = 100.dp)) {
                AsyncImage(
                    model = SharedResources.images.empty_list.drawableResId,
                    contentDescription = "Empty basket image",
                    modifier = Modifier.size(250.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = stringResource(id = SharedResources.strings.empty_basket.resourceId),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@Composable
fun TitleText(restName: String, onRestNameClick: () -> Unit = {}) {
    val annotatedString = buildAnnotatedString {
        append("Selected dishes in the ")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
            append(" ")
        }

        pushStringAnnotation(tag = "RESTAURANT", annotation = restName)
        withStyle(
            style = SpanStyle(
                color = colorResource(id = R.color.orange_price),
                fontWeight = FontWeight.Bold
            )
        ) {
            append(restName)
        }
        pop()

        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
            append(" restaurant")
        }
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .padding(vertical = 8.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "RESTAURANT", start = offset, end = offset)
                .firstOrNull()?.let {
                    onRestNameClick()
                }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketItemCard(basketItem: BasketItemModel, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val dismissState = rememberDismissState(initialValue = DismissValue.Default)

    SwipeToDismiss(
        state = dismissState,
        background = {
            if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        tint = colorResource(id = R.color.silver)
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize())
            }
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { expanded = !expanded },
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.light_silver),
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = basketItem.dishImage,
                            contentDescription = basketItem.name,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = basketItem.name,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "$${basketItem.sum}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand/Collapse Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    if (expanded) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Cutlery: ${if (basketItem.isCutlery) "Yes" else "No"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Sauces: ${if (basketItem.isSauces) "Yes" else "No"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (basketItem.userWishes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "User Wishes: ${basketItem.userWishes}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        },
        modifier = Modifier.animateContentSize()
    )

    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onDelete()
    }
}


@Composable
fun BasketError(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red)
    }
}

@Composable
fun BasketLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp))
    }
}