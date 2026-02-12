package com.example.medicinereminder.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medicinereminder.data.db.AppDatabase
import com.example.medicinereminder.repository.MedicineRepository
import com.example.medicinereminder.ui.AddMedicineScreen
import com.example.medicinereminder.ui.MedicineHomeScreen
import com.example.medicinereminder.viewmodel.MedicineViewModel
import com.example.medicinereminder.viewmodel.MedicineViewModelFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = MedicineRepository(database.medicineDao())
        val viewModelFactory = MedicineViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            val viewModel: MedicineViewModel = viewModel(
                factory = viewModelFactory
            )

            MedicineApp(viewModel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "medicine_channel",
                "Medicine Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply{
                description = "Medication reminder notifications"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun MedicineApp(viewModel: MedicineViewModel){
    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect (Unit){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable("home"){
            MedicineHomeScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate("add")},
                onEditClick = {medicineId->
                    navController.navigate("edit/$medicineId")
                }
            )
        }

        composable("add") {
            AddMedicineScreen(
                viewModel = viewModel,
                onSave = {
                    navController.popBackStack()
                }
            )
        }

        composable("Edit/{medicineId}") {backStack->
            val id = backStack.arguments?.getString("medicineId")?.toLong()

            AddMedicineScreen(
                viewModel = viewModel,
                medicineId = id,
                onSave = { navController.popBackStack()}
            )
        }
    }
}

