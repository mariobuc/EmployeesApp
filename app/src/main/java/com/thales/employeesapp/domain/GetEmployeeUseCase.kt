package com.thales.employeesapp.domain

import com.thales.employeesapp.data.repository.EmployeeRepository
import com.thales.employeesapp.model.EmployeeModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class GetEmployeeUseCase @Inject constructor(
    private val repository : EmployeeRepository
){
    suspend operator fun invoke(id: Int) = repository.getEmployeeFromAPI(id)

    fun calcAnnualSalary(employee : EmployeeModel){
        employee.employee_annual_salary = employee.employee_salary*12
    }
}