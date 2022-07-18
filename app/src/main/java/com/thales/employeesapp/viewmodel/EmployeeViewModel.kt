package com.thales.employeesapp.viewmodel


import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shimi.com.employeelist.utils.SingleLiveEvent
import com.thales.employeesapp.data.repository.EmployeeRepository
import com.thales.employeesapp.domain.GetEmployeeUseCase
import com.thales.employeesapp.domain.GetEmployeesUseCase
import com.thales.employeesapp.model.EmployeeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val getEmployeesUseCase : GetEmployeesUseCase,
    private val getEmployeeUseCase : GetEmployeeUseCase,
    private val employeeRepository: EmployeeRepository
) : ViewModel(){

    val openEmployeeDetailsEvent = SingleLiveEvent<EmployeeModel>()
    val isRefreshing = ObservableBoolean(false)

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<EmployeeUiState>(EmployeeUiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val employeeUiState: StateFlow<EmployeeUiState> = _uiState

    private fun postNewState(newState: EmployeeUiState) {
        _uiState.value = newState
    }
    private fun getState() = _uiState.value

    init {
        loadEmployees()
    }

    fun loadEmployees() {
        viewModelScope.launch {
            // Do an suspend operation to fetch Employees and post value.
            Log.d("TAG","in loadEmployees")
            getEmployeesUseCase()
            employeeRepository.getEmployeesFromDB()
                .onStart {
                    Log.d("TAG","in getEmployees onStart")
                    isRefreshing.set(true) }
                .catch {
                    Log.d("TAG","in loadEmployees catch $it")
                    postNewState(EmployeeUiState.Error(it)) }
                .collect {
                    if(it.isNotEmpty()){
                        Log.d("TAG","in loadEmployees collect $it")
                        postNewState(EmployeeUiState.Success(it))
                    }
                    isRefreshing.set(false)
                }
        }
    }


    fun openEmployeeDetails(employee: EmployeeModel) {
        openEmployeeDetailsEvent.value = employee
    }

    // Represents different states for the Employees screen
    sealed class EmployeeUiState {
        data class Success(val employees: List<EmployeeModel>): EmployeeUiState()
        data class Error(val exception: Throwable): EmployeeUiState()
    }

}