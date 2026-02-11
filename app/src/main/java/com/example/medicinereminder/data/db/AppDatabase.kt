package com.example.medicinereminder.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medicinereminder.data.dao.MedicineDao
import com.example.medicinereminder.data.local.MedicineEntity

@Database(entities = [MedicineEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    abstract fun medicineDao(): MedicineDao

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context:Context): AppDatabase{
            return INSTANCE ?:synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medicine_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}