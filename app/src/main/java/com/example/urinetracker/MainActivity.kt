package com.example.urinetracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.room.Room
import com.example.room_test.AppDatabase
import com.example.room_test.Vals
import com.example.urinetracker.ui.theme.UrineTrackerTheme
import com.example.urinetracker.ui.theme.dark
import com.example.urinetracker.ui.theme.darker
import com.example.urinetracker.ui.theme.light
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration()
            .build()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrineTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BottomBar(
                        modifier = Modifier.padding(innerPadding),
                        db
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier, db: AppDatabase) {
    var showHydrationTracking by  remember { mutableStateOf(false) }
    var showCalendar by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    configuration.screenWidthDp.dp * 0.3f

    Box(modifier = Modifier
        .fillMaxSize()
        .background(dark)
    )
    Box(modifier = Modifier
        .height(75.dp)
        .fillMaxWidth()
        .background(darker)
        .zIndex(4f))
    Row (modifier = Modifier.fillMaxSize()
        .offset(x = 0.dp, y = (configuration.screenHeightDp.dp - 100.dp))
        .background(darker)
        .width(configuration.screenWidthDp.dp)
        .height(100.dp)
        .zIndex(4f)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_glass_black),
            contentDescription = "Glass icon",
            modifier = modifier
                .size(75.dp)
                .offset(x = 35.dp, y = -25.dp)
                .clickable {
                    showHydrationTracking = !showHydrationTracking
                    showCalendar = false
                },
            tint = light
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar_black),
            contentDescription = "Calendar icon",
            modifier = modifier
                .size(75.dp)
                .offset(x = configuration.screenWidthDp.dp - 185.dp, y = -25.dp)
                .clickable {
                    showHydrationTracking = false
                    showCalendar = !showCalendar
                },
            tint = light
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_camera_black),
            contentDescription = "Camera icon",
            modifier = modifier
                .size(75.dp)
                .offset(x = 10.dp, y = -25.dp),
            tint = light
        )
    }
    if (showHydrationTracking) {
        Test(modifier, 500, db, 0)
    }
    if (showCalendar) {
        Log.v("nigga", "turned on")
        ShowCalendar(db)
    }
}

@Composable
fun ShowCalendar(db: AppDatabase) {
    CalendarApp(db)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val db = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration()
        .build()
    UrineTrackerTheme {
        BottomBar(Modifier, db)
//        calendar(Modifier, db)
//        Test(Modifier, 500, db, 0)
    }
}

suspend fun loadData(db: AppDatabase): List<Vals> {
    var valsList: List<Vals>
    val userDao = db.userDao()
    val calendar = Calendar.getInstance()
    withContext(Dispatchers.IO) {
        valsList = userDao.getAll()
    }
    if (valsList.isNotEmpty()) {
        valsList.forEach {
            Log.v("DatabaseLog", "UID: ${it.uid}, Year: ${it.year}, Month: ${it.month}, Day: ${it.day}, Hour: ${it.hour}, Minute: ${it.minute}, RawTime: ${it.rawTime}, WaterVal: ${it.waterVal}")
        }
        return valsList
    } else {
        return listOf<Vals>()
    }
}

suspend fun deleteData(db: AppDatabase, data: Vals) {
    withContext(Dispatchers.IO) {
        db.userDao().delete(data)
    }
}
