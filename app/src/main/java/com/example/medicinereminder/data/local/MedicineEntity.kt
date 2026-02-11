package com.example.medicinereminder.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines" )
data class MedicineEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicineId")
    val medicineId :Int =0,
    @ColumnInfo(name = "medicineName")
    val medicineName: String,
    @ColumnInfo(name = "medicineDosage")
    val medicineDosage:String,
    @ColumnInfo(name = "medicineTime")
    val medicineTime:Long,
    @ColumnInfo(name = "medicineIsTaken")
    val medicineIsTaken:Boolean
)