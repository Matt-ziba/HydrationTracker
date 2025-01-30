package com.example.urinetracker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.room_test.AppDatabase
import com.example.room_test.Vals
import com.example.urinetracker.ui.theme.dark
import com.example.urinetracker.ui.theme.darker
import com.example.urinetracker.ui.theme.light
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

@Composable
fun Test(modifier: Modifier = Modifier, waterValI: Int, db: AppDatabase, uid: Int) {
    val userDao = db.userDao()
    val calendar = Calendar.getInstance()
    var uid by remember { mutableIntStateOf(uid) }
    var valsList by remember { mutableStateOf<List<Vals>>(emptyList()) }
    var save by remember { mutableStateOf(false) }
    var dataread by remember { mutableStateOf(false) }
    var datadelete by remember { mutableStateOf(false) }
    var recall by remember { mutableStateOf(false) }
    var waterVal by  remember { mutableIntStateOf(waterValI) }
    var showHydrationTracking by  remember { mutableStateOf(false) }

    LaunchedEffect(save) {
        if (save) {
            withContext(Dispatchers.IO) {
                valsList = userDao.getAll()
                if(valsList.isNotEmpty()) {
                    userDao.insertAll(
                        Vals(valsList.last().uid + 1, calendar.get(Calendar.YEAR), calendar.get(
                            Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(
                            Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), System.nanoTime(), waterVal)
                    )
                } else {
                    userDao.insertAll(
                        Vals(0, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
                            Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(
                            Calendar.MINUTE), System.nanoTime(), waterVal)
                    )
                }
            }
            save = false
        }
    }
    LaunchedEffect(dataread) {
        if (dataread) {
            withContext(Dispatchers.IO) {
                valsList = userDao.getAll()
            }
            if (valsList.isNotEmpty()) {
                valsList.forEach {
                    Log.v("DatabaseLog", "UID: ${it.uid}, Year: ${it.year}, Month: ${it.month}, Day: ${it.day}, Hour: ${it.hour}, Minute: ${it.minute}, RawTime: ${it.rawTime}, WaterVal: ${it.waterVal}")
                    val emptyList = listOf<Vals>()
                    valsList = emptyList
                }
            }
            dataread = false
        }
    }

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
            .align(Alignment.Center)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .offset(x = 0.dp, y = 110.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(darker)) {
            Text(
                text = waterVal.toString(), modifier = Modifier
                    .padding(20.dp),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    fontSize = 65.sp,
                    fontWeight = FontWeight.Bold
                ), color = light
            )
        }
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
                .clickable { save = true }
                .wrapContentSize(Alignment.Center)
            ) {
                Text(text = "SAVE",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        fontSize = 65.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = light,
                    modifier = Modifier
                )
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
                    showHydrationTracking = true
                    waterVal = waterVal + 50
                },
            tint = light
        )
        Icon(painter = painterResource(id = R.drawable.ic_minus_black),
            contentDescription = "Plus Icon",
            modifier = Modifier
                .background(dark)
                .size(75.dp)
                .align(Alignment.Center)
                .offset(x = -135.dp, y = 110.dp)
                .clickable {showHydrationTracking = true
                    waterVal = waterVal - 50},
            tint = light
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_glass_black),
            contentDescription = "Glass icon",
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 0.dp, y = -150.dp),
            tint = light
        )
    }
    if (showHydrationTracking) {
        Test(modifier, waterVal, db, uid)
    }
    if(waterVal < 0) {
        Test(modifier, 50, db, uid)
        Test(modifier, 0, db, uid)
    }
}
