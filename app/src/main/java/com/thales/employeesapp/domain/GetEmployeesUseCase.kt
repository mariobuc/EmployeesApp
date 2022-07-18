package com.thales.employeesapp.domain

import com.thales.employeesapp.data.repository.EmployeeRepository
import com.thales.employeesapp.model.EmployeeModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class GetEmployeesUseCase @Inject constructor(
    private val repository : EmployeeRepository
){
    suspend operator fun invoke() = repository.getEmployeesFromAPI().catch {  }.collect()

    fun calcAnnualSalary(employees : List<EmployeeModel>){
        employees.forEach {
            it.employee_annual_salary = it.employee_salary*12
        }
    }
}