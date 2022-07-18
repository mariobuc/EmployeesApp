package com.thales.employeesapp.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EmployeeApiClient {

    @GET("employees")
    suspend fun getAllEmployees(): EmployeesRestObject

    @GET("employee/{id}")
    suspend fun getEmployee(@Path("id") EmployeeId: Int) : EmployeeRestObject

}