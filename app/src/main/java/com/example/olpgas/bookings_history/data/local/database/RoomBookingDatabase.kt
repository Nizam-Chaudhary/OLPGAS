package com.example.olpgas.bookings_history.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.olpgas.bookings_history.data.local.database.dao.RoomBookingDao
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.local.database.dao.AllRoomsDetailsDao

@Database(
    entities = [RoomBookingLocal::class],
    version = 1,
    exportSchema = false
)
abstract class RoomBookingDatabase : RoomDatabase() {
    abstract fun getRoomBookingDao() : RoomBookingDao

    companion object {
        @Volatile
        private var instance: RoomBookingDatabase? = null

        private var LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            RoomBookingDatabase::class.java,
            name = "RoomBooking.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}