package com.example.medicinereminder.ApiState

import com.example.medicinereminder.domain.model.Medicine

sealed class States {
    object Loading: States()
    data class Success(val medicines:List<Medicine>): States()
    object Empty: States()
    data class Error(val error:String): States()
}