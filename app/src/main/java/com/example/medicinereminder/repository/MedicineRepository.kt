package com.example.medicinereminder.repository

import com.example.medicinereminder.data.dao.MedicineDao
import com.example.medicinereminder.data.local.MedicineEntity
import com.example.medicinereminder.domain.model.Medicine
import com.example.medicinereminder.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicineRepository(private val dao: MedicineDao) {

    fun getMedicine(): Flow<List<Medicine>> =
        dao.getAllMedicines().map { list->
            list.map{
                Medicine(
                    medicineId = it.medicineId,
                    medicineName = it.medicineName,
                    medicineDosage = it.medicineDosage,
                    medicineTime = it.medicineTime,
                    medicineIsTaken = it.medicineIsTaken
                )
            }
        }

    suspend fun addMedicine(medicine:Medicine){
        dao.insertMedicine(
            medicine.toEntity()
        )
    }

    suspend fun updateMedicine(medicine: Medicine){
        dao.updateMedicine(
            medicine.toEntity()
        )
    }

    suspend fun markAsTaken(medicine: Medicine){
        dao.updateMedicine(
           medicine.copy(
               medicineIsTaken = !medicine.medicineIsTaken
           ).toEntity()
        )
    }

    suspend fun deleteMedicine(medicine: Medicine){
        dao.deleteMedicine(
            MedicineEntity(
                medicineId = medicine.medicineId,
                medicineName = medicine.medicineName,
                medicineDosage = medicine.medicineDosage,
                medicineTime = medicine.medicineTime,
                medicineIsTaken = medicine.medicineIsTaken
            )
        )
    }

}