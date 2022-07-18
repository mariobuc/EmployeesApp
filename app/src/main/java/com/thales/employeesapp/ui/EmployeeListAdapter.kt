package com.thales.employeesapp.ui

import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.thales.employeesapp.R
import com.thales.employeesapp.databinding.ItemEmployeeBinding
import com.thales.employeesapp.model.EmployeeModel
import com.thales.employeesapp.viewmodel.EmployeeViewModel

class EmployeeListAdapter(private var employeeList: List<EmployeeModel>,
                          private val employeeViewModel: EmployeeViewModel?)
    : RecyclerView.Adapter<EmployeeListAdapter.EmployeeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder {

        return EmployeeHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_employee, parent, false
        ) )

    }

    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) =
        holder.bind(employeeList[position], object : OnItemClickListener {
            override fun onClick(employee: EmployeeModel) {
                Log.d("TAG","in EmployeeHolder OnClickListener size = ${employeeList.size} employee = $employee")
                employeeViewModel?.openEmployeeDetails(employee)

            }
        })

    override fun getItemCount(): Int = employeeList.size


    class EmployeeHolder(private val binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(employeeEntity: EmployeeModel, employeeListener: OnItemClickListener) {
            with(binding)
            {
                employee  = employeeEntity
                listener = employeeListener
                executePendingBindings()
            }
        }
    }

    fun setEmployeeList(employeeList: List<EmployeeModel>?) {
        if(employeeList != null) {
            this.employeeList = employeeList
            notifyDataSetChanged()
        }
        Log.d("TAG","in setEmployeeList ${itemCount}")
    }

    interface OnItemClickListener {
        fun onClick(employee: EmployeeModel)
    }

}