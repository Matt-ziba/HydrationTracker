package com.example.urinetracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.urinetracker.ui.theme.UrineTrackerTheme
import com.example.urinetracker.ui.theme.dark
import com.example.urinetracker.ui.theme.darker
import com.example.urinetracker.ui.theme.light
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val botOffset = 90.dp;
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrineTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    bottomBar(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun bottomBar(name: String, modifier: Modifier = Modifier) {
    var showHydrationTracking by  remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    Box(modifier = Modifier
        .fillMaxSize()
        .background(dark)
    )
    Box(modifier = Modifier
        .height(75.dp)
        .fillMaxWidth()
        .background(darker))
    Box(modifier = Modifier
        .offset(x = 0.dp, y = (configuration.screenHeightDp.dp - 100.dp))
        .background(darker)
        .width(configuration.screenWidthDp.dp)
        .height(100.dp)
    ) {
        Row (modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.size(75.dp)
                .fillMaxHeight()
                .offset(x = 35.dp, y = 10.dp)
                .clickable {
                    if(!showHydrationTracking) {
                        showHydrationTracking = true
                    } else {
                        showHydrationTracking = false
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_glass_black),
                    contentDescription = "Glass icon",
                    modifier = modifier
                        .fillMaxSize(),
                    tint = light
                )
            }
            Box(modifier = Modifier.size(75.dp)
                .fillMaxHeight()
                .offset(x = configuration.screenWidthDp.dp - 185.dp, y = 10.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar_black),
                    contentDescription = "Calendar icon",
                    modifier = modifier
                        .fillMaxSize(),
                    tint = light
                )
            }
            Box(modifier = Modifier.size(75.dp)
                .fillMaxHeight()
                .offset(x = 10.dp, y = 10.dp)
                .clickable {
                    if (!showHydrationTracking) {
                        showHydrationTracking = true
                    } else {
                        showHydrationTracking = false
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera_black),
                    contentDescription = "Camera icon",
                    modifier = modifier
                        .fillMaxSize(),
                    tint = light
                )
            }
        }

    }
    if (showHydrationTracking) {
        HydrationTracking()
    }
}
@Composable
fun HydrationTracking() {
    var boxColor by remember { mutableStateOf(Color.Gray) }
    val configuration = LocalConfiguration.current
    Text(text = "Hydration",
        style = TextStyle(
            fontSize = 35.sp,
            fontFamily = FontFamily(Font(R.font.roboto)),
            fontWeight = FontWeight.Normal
        ),
        color = light,
        modifier = Modifier
            .offset(x = 10.dp, y = 25.dp)
            .height(100.dp)
            .fillMaxWidth()
    )
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
    ) {
        Box(modifier = Modifier
            .height(75.dp)
            .width(200.dp)
            .align(Alignment.Center)
            .wrapContentSize(Alignment.Center)
            .offset(x = 0.dp, y = 250.dp)) {
            Box(modifier = Modifier
                .height(75.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(darker)
                .clickable {  }
                .wrapContentSize(Alignment.Center)
            ) {
                Text(text = "SAVE",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        fontSize = 65.sp,
                        fontWeight = FontWeight.Bold
                    ),
                color = light,
                modifier = Modifier)
            }
        }
        Icon(painter = painterResource(id = R.drawable.ic_plus_white),
            contentDescription = "Plus Icon",
            modifier = Modifier
                .background(dark)
                .size(75.dp)
                .align(Alignment.Center)
                .offset(x = 135.dp, y = 110.dp)
                .clickable {

                },
            tint = light)
        Icon(painter = painterResource(id = R.drawable.ic_minus_black),
            contentDescription = "Plus Icon",
            modifier = Modifier
                .background(dark)
                .size(75.dp)
                .align(Alignment.Center)
                .offset(x = -135.dp, y = 110.dp)
                .clickable {  },
            tint = light)
        Icon(
            painter = painterResource(id = R.drawable.ic_glass_black),
            contentDescription = "Glass icon",
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 0.dp, y = -150.dp),
            tint = light
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UrineTrackerTheme {
        bottomBar("Nigga")
        HydrationTracking()
    }
}

fun logHydration(context: Context, amount: Int) {
    val database = DatabaseBuilder.getDatabase(context)
    val hydrationLog = HydrationLog(
        timestamp = System.currentTimeMillis(),
        amount = amount
    )

    CoroutineScope(Dispatchers.IO).launch {
        database.hydrationLogDao().insertLog(hydrationLog)
    }
}
