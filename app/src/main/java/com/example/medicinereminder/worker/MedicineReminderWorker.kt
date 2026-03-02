package com.example.medicinereminder.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Database
import androidx.work.CoroutineWorker
import androidx.work.Worker
import com.example.medicinereminder.R
import androidx.work.WorkerParameters
import com.example.medicinereminder.data.db.AppDatabase
import com.example.medicinereminder.util.TextToSpeechHelper
import com.example.medicinereminder.view.MainActivity
import kotlinx.coroutines.delay

class MedicineReminderWorker(context: Context, workerParams:WorkerParameters) : CoroutineWorker(context, workerParams){

    override suspend fun doWork(): Result{
        Log.d("MedicineWorker", "Worked triggered")

        val medicineId = inputData.getInt("MEDICINE_ID", -1)
        if(medicineId == -1) return Result.failure()

        val medicineName =
            inputData.getString("MEDICINE_NAME") ?:return Result.failure()


        val db = AppDatabase.getInstance(applicationContext)
        val dao = db.medicineDao()


        showNotification(medicineName)

        val tts = TextToSpeechHelper(applicationContext)

        val reminderDuration = System.currentTimeMillis() + 2 * 60 * 1000L
        val intervalMs = 15_000L


        while(System.currentTimeMillis() < reminderDuration){

            val medicine = dao.getMedicineById(medicineId)

            if(medicine == null || medicine.medicineIsTaken){
                break
            }
            tts.speak("It's time to take your medicine $medicineName")
            delay(intervalMs)
        }

        tts.shutdown()
        return Result.success()

    }

    private fun showNotification(name: String){

        val intent =Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            )as NotificationManager

        val notification = NotificationCompat.Builder(
            applicationContext,
            "medicine_channel"
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Medicine Reminder")
            .setContentText("Time to take $name")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }



}