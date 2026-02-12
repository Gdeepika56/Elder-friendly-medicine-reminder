package com.example.medicinereminder.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicinereminder.domain.model.Medicine
import com.example.medicinereminder.util.AccessibilityUtils.accessibleDescription
import com.example.medicinereminder.util.TimeUtils


@Composable
fun MedicineItem(medicine: Medicine, onTakenClick:() -> Unit, onDeleteClick:() ->Unit, onEditClick:(Medicine) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{ onEditClick(medicine)},
        elevation = CardDefaults.cardElevation(6.dp)
    ){
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (modifier = Modifier.weight(1f)){
                Text(
                    text = medicine.medicineName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration =
                        if(medicine.medicineIsTaken)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None,
                    color =
                        if(medicine.medicineIsTaken)
                            Color.Gray
                        else
                            Color.Black
                )

                Text(
                    text = "Dosage: ${medicine.medicineDosage}",
                    color = Color.DarkGray
                )

                Text(
                    text = "Time: ${TimeUtils.formatTime(medicine.medicineTime)}",
                    color = Color.DarkGray
                )
            }

            Checkbox(
                checked = medicine.medicineIsTaken,
                onCheckedChange ={ onTakenClick() },
                modifier = Modifier.accessibleDescription(
                    if(medicine.medicineIsTaken)
                       "Medicine taken"
                    else
                       "Mark medicine as taken"
                )
            )

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Medicine")
            }
        }
    }

}
