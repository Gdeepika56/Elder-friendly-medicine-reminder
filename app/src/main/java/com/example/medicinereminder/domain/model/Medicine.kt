package com.example.medicinereminder.domain.model

data class Medicine(
    val medicineId: Int =0,
    val medicineName :String,
    val medicineDosage:String,
    val medicineTime:Long,
    val medicineIsTaken:Boolean = false
)