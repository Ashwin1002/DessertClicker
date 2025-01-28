package com.example.dessertclicker

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.theme.DessertClickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onResume Called")
        enableEdgeToEdge()
        setContent {
            DessertClickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().statusBarsPadding()
                ) {
                    DesertClickerApp(Datasource.dessertList)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}

fun determineDessertToShow(
    desserts: List<Dessert>,
    dessertsSold: Int
): Dessert {
    var dessertToShow = desserts.first()
    for (dessert in desserts) {
        if (dessertsSold >= dessert.startProductionAmount) {
            dessertToShow = dessert
        } else {
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            break
        }
    }

    return dessertToShow
}


/**
 * Share desserts sold information using ACTION_SEND intent
 */
private fun shareSoldDessertsInformation(intentContext: Context, dessertsSold: Int, revenue: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            intentContext.getString(R.string.share_text, dessertsSold, revenue)
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    try {
        ContextCompat.startActivity(intentContext, shareIntent, null)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            intentContext,
            intentContext.getString(R.string.sharing_not_available),
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
fun DesertClickerApp(desserts: List<Dessert>) {
    var revenue by rememberSaveable { mutableIntStateOf(0) }
    var desertSold by rememberSaveable { mutableIntStateOf(0) }

    val currentDessertIndex by rememberSaveable { mutableIntStateOf(0) }

    var currentDessertPrice by rememberSaveable {
        mutableIntStateOf(desserts[currentDessertIndex].price)
    }
    var currentDessertImageId by rememberSaveable {
        mutableIntStateOf(desserts[currentDessertIndex].imageRes)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val intentContext = LocalContext.current
            DessertAppbar (
                onShareButtonClicked = {
                    shareSoldDessertsInformation(
                        intentContext = intentContext,
                        dessertsSold = desertSold,
                        revenue = revenue
                    )
                },
                modifier = Modifier
                    .fillMaxWidth().heightIn(min = 60.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        innerPadding ->

        Surface(modifier = Modifier.padding(innerPadding)) {
            DesertClickerBody(
                revenue = revenue,
                dessertsSold = desertSold,
                dessertImageId = currentDessertImageId,
                onDessertClicked = {
                    // Update the revenue
                    revenue += currentDessertPrice

                    desertSold++

                    val currentDessert = determineDessertToShow(desserts, desertSold)

                    currentDessertImageId = currentDessert.imageRes
                    currentDessertPrice = currentDessert.price
                },

            )
        }
    }
}

@Composable
fun DesertClickerBody(
    revenue: Int,
    dessertsSold: Int,
    @DrawableRes dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    
    Box (
        modifier = modifier,
    ){
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            modifier = modifier.fillMaxWidth().fillMaxHeight(),
            contentScale = ContentScale.Crop
        )
        BakeryItem(
            imageRes = dessertImageId,
            modifier = Modifier
                .width(dimensionResource(R.dimen.image_size))
                .height(dimensionResource(R.dimen.image_size))
                    .align(Alignment.Center).offset(y = -(60).dp),
            onClick = onDessertClicked
        )
        Column (
            modifier = modifier
                .fillMaxWidth()
                .fillMaxSize(.2f)
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .padding(all = 16.dp)
                .align(Alignment.BottomEnd),
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth()
            ){
                Text(
                    stringResource(R.string.dessert_sold),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp
                    ))

                Text(
                    "$dessertsSold",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp

                    )
                )
            }
            Spacer(modifier = Modifier.padding(bottom = 24.dp))
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth()
            ){
                Text(
                    stringResource(R.string.total_revenue),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    "$$revenue",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Composable
fun BakeryItem(@DrawableRes imageRes: Int, modifier: Modifier,  onClick: () -> Unit) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clickable {
            onClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DessertAppbar(modifier: Modifier = Modifier, onShareButtonClicked: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium)),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(
            onClick = onShareButtonClicked
        ) {
            Icon(
                Icons.Filled.Share,
                contentDescription = "Share Icon"
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DessertClickerTheme {
        DesertClickerApp(Datasource.dessertList)
    }
}