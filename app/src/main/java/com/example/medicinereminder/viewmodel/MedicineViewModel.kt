package com.example.medicinereminder.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.medicinereminder.ApiState.States
import com.example.medicinereminder.domain.model.Medicine
import com.example.medicinereminder.repository.MedicineRepository
import com.example.medicinereminder.util.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MedicineViewModel(private val repository: MedicineRepository): ViewModel() {

    private val _uiState = MutableStateFlow<States>(States.Loading)
    val uiState: StateFlow<States> = _uiState

    init {
        loadMedicines()
    }

    private fun loadMedicines(){
        viewModelScope.launch {
            repository.getMedicine()
                .catch {
                    _uiState.value=
                        States.Error("Unable to load medicines")
                }
                .collect { medicines->
                    _uiState.value =
                        if(medicines.isEmpty())
                            States.Empty
                        else
                            States.Success(medicines)
                }
        }
    }

    fun addMedicines(context: Context, medicine:Medicine){
       viewModelScope.launch {
           repository.addMedicine(medicine)
           ReminderScheduler.schedule(context,medicine)
       }
    }

    fun onMedicineTaken(context:Context,medicine: Medicine){
        viewModelScope.launch {
            repository.markAsTaken(medicine)
            ReminderScheduler.schedule(context,medicine)
        }
    }

    fun updateMedicine(context: Context, medicine: Medicine){
        viewModelScope.launch{
            repository.updateMedicine(medicine)
            ReminderScheduler.schedule(context,medicine)
        }
    }

    fun getMedicineById(id: Long): Medicine? {
        val state = _uiState.value
        return if (state is States.Success) {
            state.medicines.find { it.medicineId.toLong() == id }
        } else null
    }


    fun deleteMedicine(medicine: Medicine){
        viewModelScope.launch {
            repository.deleteMedicine(medicine)
        }
    }
}

class MedicineViewModelFactory(private val repository: MedicineRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass:Class<T>): T{
        if(modelClass.isAssignableFrom(MedicineViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MedicineViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

