package com.example.room_test

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity
data class Vals(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "week") val week: Int,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "hour") val hour: Int,
    @ColumnInfo(name = "minute") val minute: Int,
    @ColumnInfo(name = "raw_time") val rawTime: Long,
    @ColumnInfo(name = "water_val") val waterVal: Int
)

@Dao
interface UserDao {
    @Query("SELECT * FROM vals")
    fun getAll(): List<Vals>

    @Query("SELECT * FROM vals")
    fun getAllFlow(): Flow<List<Vals>>

    @Query("SELECT * FROM vals WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Vals>

    @Query("SELECT * FROM Vals WHERE year = :year AND month = :month AND day = :day")
    fun getValsByDate(year: Int, month: Int, day: Int): List<Vals>

    @Query("SELECT * FROM Vals WHERE year = :year AND week = :week")
    fun getValsByWeek(year: Int, week: Int): List<Vals>

    @Query("SELECT * FROM vals WHERE year LIKE :first AND " +
            "month LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Vals

    @Update
    suspend fun updateVals(vals: Vals)

    @Insert
    fun insertAll(vararg vals: Vals)

    @Delete
    fun delete(vals: Vals)
}

@Database(entities = [Vals::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
