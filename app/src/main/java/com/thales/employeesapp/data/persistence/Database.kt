package app.shimi.com.employeelist.data.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thales.employeesapp.model.EmployeeModel


@Database(entities = [EmployeeModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}