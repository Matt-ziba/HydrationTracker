package com.example.urinetracker

import android.annotation.SuppressLint
import android.util.Log
import android.util.Property
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

fun fakeData(): List<Vals> {
    return listOf(
        Vals(0, 2008, 6, 26, 15, 8, 546548, 420),
        Vals(1, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(2, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(3, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(4, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(5, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(6, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(7, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(8, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(9, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(10, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(11, 2008, 6, 26, 15, 24, 546548, 420),
        Vals(12, 2008, 6, 26, 15, 24, 546548, 420)

    )
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    var showCalendar by remember { mutableStateOf(false) }
    val db = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration()
        .build()
    UrineTrackerTheme { CalendarList(modifier = Modifier, fakeData(), db, mutableStateOf(showCalendar)) }
}

@Composable
fun CalendarApp(db: AppDatabase, showCalendar: MutableState<Boolean>) {
    var data by remember { mutableStateOf<List<Vals>>(emptyList()) }
    LaunchedEffect(Unit) {
        data = loadData(db)
    }
    CalendarList(modifier = Modifier, data, db, showCalendar)
}

@Composable
fun CalendarList(modifier: Modifier = Modifier, dataList: List<Vals>, db: AppDatabase, showCalendar: MutableState<Boolean>) {
    LazyColumn(modifier = modifier
        .offset(x = 0.dp, y = 75.dp)
        .height(740.dp)) {
        items(dataList) { data ->
            ValCard(
                data = data,
                modifier = Modifier.padding(8.dp),
                db,
                showCalendar
            )
        }
    }
}

@Composable
fun ValCard(data: Vals, modifier: Modifier = Modifier, db: AppDatabase, showCalendar: MutableState<Boolean>) {
    var datadelete by remember { mutableStateOf(false) }
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
                        .zIndex(2f)
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
    if(datadelete) {
        LaunchedEffect(Unit) {
            deleteData(db, data)
        }
        Log.v("nigga", "deleted")
        showCalendar = !showCalendar
    }

}
