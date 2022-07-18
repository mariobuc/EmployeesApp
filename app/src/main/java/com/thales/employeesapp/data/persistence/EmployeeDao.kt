package app.shimi.com.employeelist.data.persistence.db


import androidx.room.*
import com.thales.employeesapp.model.EmployeeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees")
    fun getEmployees(): Flow<List<EmployeeModel>>

    @Query("SELECT * FROM employees WHERE id = :id")
    suspend fun getEmployee(id: Int):EmployeeModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(employees: List<EmployeeModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: EmployeeModel)
}