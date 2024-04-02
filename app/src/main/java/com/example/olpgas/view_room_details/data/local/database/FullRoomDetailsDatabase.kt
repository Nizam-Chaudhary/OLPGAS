package com.example.olpgas.view_room_details.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.olpgas.view_room_details.data.local.database.dao.FullRoomDetailsDao
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.data.local.database.type_converters.Converters

@Database(
    entities = [FullRoomDetailsLocal::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class FullRoomDetailsDatabase : RoomDatabase() {
    abstract fun getFullRoomDetailsDao(): FullRoomDetailsDao

    companion object {
        @Volatile
        private var instance: FullRoomDetailsDatabase? = null

        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            FullRoomDetailsDatabase::class.java,
            "FullRoomDetails.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
