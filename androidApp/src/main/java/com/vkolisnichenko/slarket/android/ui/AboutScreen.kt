package com.vkolisnichenko.slarket.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkolisnichenko.slarket.android.MyApplicationTheme
import com.vkolisnichenko.slarket.resources.SharedResources


@Composable
fun AboutScreen(backClickCallback :()-> Unit){
    Column {
        TopBar(backClickCallback = backClickCallback)
        AboutScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(backClickCallback :()-> Unit) {
    TopAppBar(title = {
        Text(text = "About")
    },
        navigationIcon = {
            IconButton(onClick = backClickCallback) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Up Button",
                )
            }
        })
}

@Composable
fun AboutScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = SharedResources.images.app_info.drawableResId),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = SharedResources.strings.app_info_screen_text.resourceId),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun DeviceInfoPreview(){
    MyApplicationTheme {
        AboutScreen {}
    }
}