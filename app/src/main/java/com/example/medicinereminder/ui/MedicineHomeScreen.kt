package com.example.medicinereminder.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext

import com.example.medicinereminder.ApiState.States
import com.example.medicinereminder.view.MainActivity
import com.example.medicinereminder.viewmodel.MedicineViewModel


@Composable
fun MedicineHomeScreen(viewModel: MedicineViewModel, onAddClick:() -> Unit, onEditClick:(Long)-> Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold (floatingActionButton = {
        FloatingActionButton(onClick = onAddClick) {
            Icon(Icons.Default.Add, contentDescription ="Add Medicine" )
        }
    }){ padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )

        when(uiState){
            is States.Loading ->{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }

            is States.Empty->{
                Box(
                    modifier= Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text("No Medicine added yet ")
                }
            }

            is States.Success->{
                val medicines = (uiState as States.Success).medicines

                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ){
                   items (medicines){ medicines->
                       MedicineItem(
                           medicine = medicines,
                           onTakenClick = {
                               viewModel.onMedicineTaken(context, medicines)
                           },
                           onDeleteClick = {
                               viewModel.deleteMedicine(medicines)
                           },
                           onEditClick = {
                               onEditClick(it.medicineId.toLong())
                           }
                       )
                   }
                }

            }

            is States.Error->{
                Text("Something went wrong")
            }
        }
    }

}
