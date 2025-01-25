package com.example.urinetracker

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "hydration_logs")
data class HydrationLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val amount: Int
)

@Dao
interface HydrationLogDao {
    @Insert
    suspend fun insertLog(hydrationLog: HydrationLog)

    @Query("SELECT * FROM hydration_logs ORDER BY timestamp DESC")
    suspend fun getAllLogs(): List<HydrationLog>

    @Query("DELETE FROM hydration_logs")
    suspend fun clearLogs()
}

@Database(entities = [HydrationLog::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hydrationLogDao(): HydrationLogDao
}

object DatabaseBuilder {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hydration_logs_db"
                ).build()
            }
        }
        return INSTANCE!!
    }
}