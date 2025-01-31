package com.example.urinetracker

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.room.Room
import com.example.room_test.AppDatabase
import com.example.room_test.Vals
import com.example.urinetracker.ui.theme.UrineTrackerTheme
import com.example.urinetracker.ui.theme.darker
import com.example.urinetracker.ui.theme.light
import com.example.urinetracker.ui.theme.transparent


var sortOrder: MutableState<String> = mutableStateOf("alltime")

fun fakeData(): List<Vals> {
    return listOf(
        Vals(0, 2008, 6, 26, 15, 8, 546548, 420, 420),
        Vals(1, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(2, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(3, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(4, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(5, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(6, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(7, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(8, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(9, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(10, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(11, 2008, 6, 26, 15, 24, 546548, 420, 420),
        Vals(12, 2008, 6, 26, 15, 24, 546548, 420, 420)

    )
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    val db = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration()
        .build()
    UrineTrackerTheme { CalendarList(modifier = Modifier, fakeData(), db) }
    CalendarApp(db)
//    ButtonList(Modifier)
}

@Composable
fun CalendarApp(db: AppDatabase) {
    var data by remember { mutableStateOf<List<Vals>>(emptyList()) }
    LaunchedEffect(Unit) {
        data = loadData(db)
    }
    CalendarList(modifier = Modifier, data, db)
    Box(modifier = Modifier
        .offset(0.dp, 23.dp)
        .width(500.dp)
        .height(50.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(darker)
        .padding(7.5.dp)
        .zIndex(4f)
    ) {
        Text(
            text = "SORT BY:",
            style = TextStyle(
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
                background = darker
            ),
            color = light,
        )
        Text(
            text = "DAY",
            style = TextStyle(
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
                background = darker
            ),
            color = light,
            modifier = Modifier
                .offset(x = 115.dp, y = 0.dp)
                .clickable {
                    sortOrder.value = "today"
                    showCalendar.value = false
                }
        )
        Text(
            text = "WEEK",
            style = TextStyle(
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
                background = darker
            ),
            color = light,
            modifier = Modifier
                .offset(x = 185.dp, y = 0.dp)
                .clickable {
                    sortOrder.value = "week"
                    showCalendar.value = false
                }
        )
        Text(
            text = "ALL TIME",
            style = TextStyle(
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
                background = darker
            ),
            color = light,
            modifier = Modifier
                .offset(x = 270.dp, y = 0.dp)
                .clickable {
                    sortOrder.value = "alltime"
                    showCalendar.value = false
                }
        )
    }

}


@Composable
fun CalendarList(modifier: Modifier = Modifier, dataList: List<Vals>, db: AppDatabase) {
    Log.v("CalendarTest", "CalendarList")
    LazyColumn(
        modifier = modifier
            .offset(x = 0.dp, y = 75.dp)
            .height(740.dp)
    ) {
        items(dataList.reversed()) { data ->
            ValCard(
                data = data,
                modifier = Modifier.padding(8.dp),
                db
            )
        }
    }
}

@Composable
fun ValCard(data: Vals, modifier: Modifier = Modifier, db: AppDatabase) {
    var datadelete by remember { mutableStateOf(false) }
    var minus by remember { mutableStateOf(false) }
    var plus by remember { mutableStateOf(false) }
    val dataText: String = if(data.minute.toString().length == 1 ) {
        data.hour.toString() + ":0" + data.minute.toString() + "         " + data.year.toString() + "." + (data.month + 1).toString() + "." + data.day.toString()
    } else {
        data.hour.toString() + ":" + data.minute.toString() + "         " + data.year.toString() + "." + (data.month + 1).toString() + "." + data.day.toString()
    }


    Card(modifier = modifier) {
        Column(modifier = Modifier) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp))
                .height(125.dp)
                .background(darker,
                    shape = RoundedCornerShape(12.dp))
                .zIndex(0f)) {
                Text(
                    text = data.waterVal.toString() + "ml",
                    modifier = Modifier.padding(8.dp)
                        .offset(x = 80.dp, y = 0.dp)
                        .zIndex(2f)
                        .background(transparent),
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 65.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        fontWeight = FontWeight.Normal
                    )
                )
                Text(
                    text = dataText,
                    modifier = Modifier.padding(8.dp)
                        .offset(x = 80.dp, y = 72.5.dp)
                        .zIndex(2f)
                        .background(transparent),
                    color = light,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        fontWeight = FontWeight.Normal
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus_white),
                    contentDescription = "Add",
                    tint = light,
                    modifier = Modifier
                        .background(transparent)
                        .size(60.dp)
                        .offset(x = 10.dp, y = 2.5.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .zIndex(2f)
                        .clickable {
                            plus = true
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_minus_black),
                    contentDescription = "Subtract",
                    tint = light,
                    modifier = Modifier
                        .background(transparent)
                        .size(60.dp)
                        .offset(x = 10.dp, y = 62.5.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .zIndex(3f)
                        .clickable {
                            minus = true
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_trash_black),
                    contentDescription = "Delete",
                    tint = light,
                    modifier = Modifier
                        .background(transparent)
                        .size(60.dp)
                        .offset(x = 305.dp, y = 15.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .zIndex(2f)
                        .clickable {
                           datadelete = true
                        }
                )
            }

        }
    }
    if(plus) {
        Log.v("data", "Added")
        if(data.waterVal < 9950) {
            LaunchedEffect(Unit) {
                db.userDao().updateVals(data.copy(waterVal = data.waterVal + 50))
            }
            showCalendar.value = false
        }
    }
    if(minus) {
        Log.v("data", "Subtracted")
        if(data.waterVal > 0) {
            LaunchedEffect(Unit) {
                db.userDao().updateVals(data.copy(waterVal = data.waterVal - 50))
            }
            showCalendar.value = false
        }
    }
    if(datadelete) {
        LaunchedEffect(Unit) {
            deleteData(db, data)
        }
        showCalendar.value = false
        Log.v("nigga", "deleted")
    }

}
