package com.thales.employeesapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.thales.employeesapp.R
import com.thales.employeesapp.databinding.ActivityEmployeeListBinding
import com.thales.employeesapp.model.EmployeeModel
import com.thales.employeesapp.model.EmployeeProvider
import com.thales.employeesapp.ui.EmployeeFragment
import com.thales.employeesapp.ui.EmployeeListAdapter
import com.thales.employeesapp.ui.EmployeeListFragment
import com.thales.employeesapp.viewmodel.EmployeeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_employee_list.*
import java.net.IDN


@AndroidEntryPoint
class EmployeeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_list)

        val bundle = intent.extras
        val id = bundle?.get("ID")

        if (savedInstanceState == null) {
            if(id.toString()=="")
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container,EmployeeListFragment(), EmployeeListFragment.TAG).commit()
            else{
                showEmployee(EmployeeModel(id.toString().toInt(), "",0,0, 0))
            }
        }

    }

    // Shows the employee detail
    fun showEmployee(employee: EmployeeModel) {
        supportFragmentManager.beginTransaction().addToBackStack("employee").replace(
            R.id.fragment_container, EmployeeFragment.forEmployee(employee.id), null).commit()
    }

}