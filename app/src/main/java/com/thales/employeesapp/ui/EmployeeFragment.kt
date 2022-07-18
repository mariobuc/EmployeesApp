package com.thales.employeesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import com.thales.employeesapp.R
import com.thales.employeesapp.domain.GetEmployeeUseCase
import com.thales.employeesapp.model.EmployeeModel
import com.thales.employeesapp.view.EmployeeListActivity
import com.thales.employeesapp.viewmodel.EmployeeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_employee.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class EmployeeFragment : Fragment() {

    @Inject
    lateinit var employeeDao: EmployeeDao

    @Inject
    lateinit var getEmployeeUseCase: GetEmployeeUseCase

    private val employeeViewModel: EmployeeViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.employee_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as EmployeeListActivity).supportActionBar?.title = "Employee Details"
        initObserver()
        extractIdAndUpdateUi()
    }

    private fun initObserver() {
        lifecycleScope.launchWhenResumed {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            employeeViewModel.employeeUiState.collect {
                // New value received
                when (it) {
                    is EmployeeViewModel.EmployeeUiState.Success -> extractIdAndUpdateUi()
                    is EmployeeViewModel.EmployeeUiState.Error -> showError(it.exception)
                }
            }
        }
    }

    private fun showError(exception: Throwable) {

    }

    private var mEmployee: EmployeeModel? = null

    private fun extractIdAndUpdateUi() {
        val bundle = arguments
        if (bundle != null && bundle.containsKey(EMPLOYEE_ID)) {
            val id = bundle.getInt(EMPLOYEE_ID)
            lifecycleScope.launch {
                getEmployeeUseCase(id)
                mEmployee = employeeDao.getEmployee(id)
                updateUi()
            }
        }
    }

    private fun updateUi() {
        employee_name.text = mEmployee?.employee_name
        employee_salary.text = mEmployee?.employee_salary.toString()
        employee_age.text = mEmployee?.employee_age.toString()
        activity?.runOnUiThread {
        }
    }

    companion object {

        private val EMPLOYEE_ID = "employee_id"

        /** Creates employee fragment for specific Employee ID  */
        fun forEmployee(employeeId: Int): EmployeeFragment {
            val fragment = EmployeeFragment()
            val args = Bundle()
            args.putInt(EMPLOYEE_ID, employeeId)
            fragment.setArguments(args)
            return fragment
        }
    }

}