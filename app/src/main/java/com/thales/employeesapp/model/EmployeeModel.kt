package com.thales.employeesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Employees")
data class EmployeeModel (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val employee_name: String,
    @ColumnInfo(name = "age")
    var employee_salary: Int,
    @ColumnInfo(name = "salary")
    val employee_age: Int,
    @ColumnInfo(name = "annual_salary")
    var employee_annual_salary: Int
)

data class RestObject (
    @SerializedName("status"  )
    var status  : String? = "",
    @SerializedName("message" )
    var message : String? = ""
) { fun isSuccess() = status.isSuccess() }

data class EmployeesRestObject (
    @SerializedName("status"  )
    var status  : String? = "",
    @SerializedName("data")
    var data : ArrayList<Data> = arrayListOf(),
    @SerializedName("message" )
    var message : String? = ""
) { fun isSuccess() = status.isSuccess()
    fun toEmployeeList() = data.map{it.toEmployeeModel()} }

data class Data (
    @SerializedName("id")
    var id : String? = "",
    @SerializedName("employee_name")
    var employeeName : String? = "",
    @SerializedName("employee_salary")
    var employeeSalary : String? = "",
    @SerializedName("employee_age")
    var employeeAge : String? = "",
    @SerializedName("profile_image")
    var profileImage : String? = ""
){
    fun toEmployeeModel() : EmployeeModel{
        return EmployeeModel(
            id = this.id!!.toIntOrNull() ?: 0,
            employee_name = this.employeeName!!,
            employee_age = this.employeeAge!!.toIntOrNull() ?: 0,
            employee_salary = this.employeeSalary!!.toIntOrNull() ?: 0,
            employee_annual_salary = 0
        )
    }
}

data class EmployeeRestObject (
    @SerializedName("status"  )
    var status  : String? = "",
    @SerializedName("data"    )
    var data : EmployeeData = EmployeeData(),
    @SerializedName("message" )
    var message : String? = ""
) { fun isSuccess() = status.isSuccess() }

data class EmployeeData (

    @SerializedName("id")
    var id : String? = "",
    @SerializedName("employee_name")
    var employeeName : String? = "",
    @SerializedName("employee_salary")
    var employeeSalary : String? = "",
    @SerializedName("employee_age")
    var employeeAge : String? = "",
    @SerializedName("profile_image")
    var profileImage : String? = ""
){
    fun toEmployeeModel() : EmployeeModel{
        return EmployeeModel(
            id = this.id!!.toIntOrNull() ?: 0,
            employee_name = this.employeeName!!,
            employee_age = this.employeeAge!!.toIntOrNull() ?: 0,
            employee_salary = this.employeeSalary!!.toIntOrNull() ?: 0,
            employee_annual_salary = 0
        )
    }
}

private fun String?.isSuccess():Boolean {
    return this == "success"
}