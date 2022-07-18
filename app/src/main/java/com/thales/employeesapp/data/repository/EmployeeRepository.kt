package com.thales.employeesapp.data.repository

import android.util.Log
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import com.thales.employeesapp.model.EmployeeApiClient
import com.thales.employeesapp.model.EmployeeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeRepository(private val api: EmployeeApiClient, val employeeDao: EmployeeDao) {

    suspend fun getEmployeesFromDB(): Flow<List<EmployeeModel>> {
        Log.d("TAG","in getEmployees")
        return employeeDao.getEmployees()
    }

    private suspend fun storeEmployeesInDb(employees: List<EmployeeModel>) {
        Log.d("TAG","in storeEmployeesInDb $employees")
        employeeDao.insertAll(employees)
    }

    private suspend fun storeEmployeeInDb(employee: EmployeeModel) {
        Log.d("TAG","in storeEmployeeInDb $employee")
        employeeDao.insert(employee)
    }

    suspend fun getEmployeesFromAPI():  Flow<List<EmployeeModel>> {

        return flow {
            val res = api.getAllEmployees()
            Log.d("TAG","in getEmployeesFromApi res =  ${res.isSuccess()}")
            if(res.isSuccess()){
                storeEmployeesInDb(res.toEmployeeList())
            }
            emit(res.toEmployeeList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEmployeeFromAPI(id: Int): Flow<EmployeeModel> {

        return flow {
            val employee : EmployeeModel
            val response = api.getEmployee(id)
            (if( response.isSuccess() ){
                employee = response.data.toEmployeeModel()
                storeEmployeeInDb(employee)
            }
            else
                employee = EmployeeModel(0,"",0,0))!!
            emit(employee)
        }.flowOn(Dispatchers.IO)
    }


}