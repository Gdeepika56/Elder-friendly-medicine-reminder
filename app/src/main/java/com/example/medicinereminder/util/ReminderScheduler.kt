package com.example.medicinereminder.util

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.example.medicinereminder.domain.model.Medicine
import com.example.medicinereminder.worker.MedicineReminderWorker
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    private fun workName(id:Int) ="medicine_$id"

    fun schedule(context: Context, medicine: Medicine){

        val delay = medicine.medicineTime - System.currentTimeMillis()

        if(delay <= 0 )return

        val data = workDataOf(
            "MEDICINE_ID" to medicine.medicineId,
            "MEDICINE_NAME" to medicine.medicineName
        )

        val request =
            OneTimeWorkRequestBuilder<MedicineReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                workName(medicine.medicineId),
                ExistingWorkPolicy.REPLACE,
                request
            )

    }

    fun snooze(context: Context, medicine:Medicine, snoozeMs:Long){
        cancel(context,medicine.medicineId)

        val newMedicine =medicine.copy(
            medicineTime = System.currentTimeMillis() + snoozeMs
        )

        schedule(context,newMedicine)
    }

    fun cancel(context: Context, id:Int){
        WorkManager.getInstance(context)
            .cancelUniqueWork(workName(id))
    }

}