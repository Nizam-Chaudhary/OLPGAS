package com.example.olpgas.browse_rooms.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.olpgas.browse_rooms.data.local.database.dao.AllRoomsDetailsDao
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.core.util.domain.states.type_converters.Converters

@Database(
    entities = [AllRoomsDetailsLocal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BrowseRoomDatabase : RoomDatabase() {
    abstract fun getAllRoomDetailsDao() : AllRoomsDetailsDao

    companion object {
        @Volatile
        private var instance: BrowseRoomDatabase? = null

        private var LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            BrowseRoomDatabase::class.java,
            name = "AllRoomDetails.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}