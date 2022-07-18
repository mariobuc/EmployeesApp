package com.thales.employeesapp.data.repository

import android.util.Log
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import com.thales.employeesapp.domain.GetEmployeeUseCase
import com.thales.employeesapp.domain.GetEmployeesUseCase
import com.thales.employeesapp.model.EmployeeApiClient
import com.thales.employeesapp.model.EmployeeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeRepository(private val api: EmployeeApiClient, val employeeDao: EmployeeDao) {

    @Inject
    lateinit var getEmployeesUseCase : GetEmployeesUseCase

    @Inject
    lateinit var getEmployeeUseCase : GetEmployeeUseCase

    suspend fun getEmployeesFromDB(): Flow<List<EmployeeModel>> {
        Log.d("TAG","in getEmployees")
        return employeeDao.getEmployees()
    }

    private suspend fun storeEmployeesInDb(employees: List<EmployeeModel>) {
        Log.d("TAG","in storeEmployeesInDb $employees")
        employeeDao.insertAll(employees)
    }

    private suspend fun storeEmployeeInDb(employee: EmployeeModel) {
        Log.d("TAG","in storeEmployeeSingleInDb $employee")
        employeeDao.insert(employee)
    }

    suspend fun getEmployeesFromAPI():  Flow<List<EmployeeModel>> {

        var employees : List<EmployeeModel>

        return flow {
            val res = api.getAllEmployees()
            Log.d("TAG","in getEmployeesFromApi res =  ${res.isSuccess()}")
            if(res.isSuccess()){
                employees = res.toEmployeeList()
                getEmployeesUseCase.calcAnnualSalary(employees)
                storeEmployeesInDb(employees)
            }
            emit(res.toEmployeeList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEmployeeFromAPI(id: Int): Flow<EmployeeModel> {

        return flow {
            val employee : EmployeeModel
            val response = api.getEmployee(id)
            Log.d("TAG","in getEmployeeFromApi res =  ${response.isSuccess()}")
            (if( response.isSuccess() ){
                employee = response.data.toEmployeeModel()
                getEmployeeUseCase.calcAnnualSalary(employee)
                storeEmployeeInDb(employee)
            }
            else
                employee = EmployeeModel(0,"",0,0, 0))!!
            emit(employee)
        }.flowOn(Dispatchers.IO)
    }


}