package com.example.medicinereminder.mapper

import com.example.medicinereminder.data.local.MedicineEntity
import com.example.medicinereminder.domain.model.Medicine

fun Medicine.toEntity(): MedicineEntity =
    MedicineEntity(
        medicineId = medicineId,
        medicineName = medicineName,
        medicineDosage = medicineDosage,
        medicineTime = medicineTime,
        medicineIsTaken = medicineIsTaken
    )