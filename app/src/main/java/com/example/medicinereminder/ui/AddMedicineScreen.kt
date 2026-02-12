package com.example.medicinereminder.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.medicinereminder.domain.model.Medicine
import com.example.medicinereminder.viewmodel.MedicineViewModel
import java.util.Calendar

@Composable
fun AddMedicineScreen(viewModel:MedicineViewModel, medicineId: Long? = null, onSave:() -> Unit) {

    val context = LocalContext.current

    val existingMedicine = medicineId?.let {
        viewModel.getMedicineById(it)
    }

    var name by remember { mutableStateOf(existingMedicine?.medicineName?: "") }
    var dosage by remember { mutableStateOf(existingMedicine?.medicineDosage?:"") }
    var selectedTime by remember { mutableStateOf(existingMedicine?.medicineTime?:0L) }

    val calendar = remember { Calendar.getInstance()}

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ){
        Text(
            text = if(existingMedicine == null) "Add Medicine" else "Edit Medicine",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value=name,
            onValueChange = { name = it },
            label ={ Text("Medicine Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value=dosage,
            onValueChange = { dosage = it },
            label ={ Text("Dosage") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick={
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND,0)
                        selectedTime = calendar.timeInMillis
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false

                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if(selectedTime == 0L) "Select Time"
                else "Change Time"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val medicine = Medicine(
                    medicineId = existingMedicine?.medicineId?:0,
                    medicineName = name,
                    medicineDosage = dosage,
                    medicineTime = selectedTime,
                    medicineIsTaken = existingMedicine?.medicineIsTaken?:false
                )

                if(existingMedicine == null){
                    viewModel.addMedicines(context,medicine)
                }else{
                    viewModel.updateMedicine(context,medicine)
                }

                onSave()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && dosage.isNotBlank() && selectedTime !=0L
        ) {
            Text(if(existingMedicine == null) "Save Medicine" else "Update Medicine")
        }
    }
}
