package com.thales.employeesapp.core

import android.content.Context
import androidx.room.Room
import app.shimi.com.employeelist.data.persistence.db.AppDatabase
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "employee-database5").build()
    }

    @Provides
    fun provideEmployeeDao(appDatabase: AppDatabase): EmployeeDao {
        return appDatabase.employeeDao()
    }

}
