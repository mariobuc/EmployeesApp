package com.thales.employeesapp.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeProvider @Inject constructor(){

        var employees: List<EmployeeModel> = emptyList()
        var employee : EmployeeModel = EmployeeModel(0,"",0,0, 0)

}