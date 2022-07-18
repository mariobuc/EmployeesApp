package com.thales.employeesapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.thales.employeesapp.R
import com.thales.employeesapp.databinding.EmployeeFragmentListBinding
import com.thales.employeesapp.model.EmployeeModel
import com.thales.employeesapp.view.EmployeeListActivity
import com.thales.employeesapp.view.MainActivity
import com.thales.employeesapp.viewmodel.EmployeeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.employee_fragment_list.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmployeeListFragment : androidx.fragment.app.Fragment() {

    private val employeeViewModel: EmployeeViewModel by activityViewModels()
    private val employeeListAdapter: EmployeeListAdapter by lazy { EmployeeListAdapter(listOf(),employeeViewModel) }

    private lateinit var mItemAnimation: LayoutAnimationController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding = EmployeeFragmentListBinding.inflate(inflater, container, false).apply {
            this.viewModel = employeeViewModel
            this.adapter = employeeListAdapter
        }
        (requireActivity() as EmployeeListActivity).supportActionBar?.title = "Employee List"
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initDataObserver()
    }


    private fun initDataObserver() {

        lifecycleScope.launch {
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                employeeViewModel.employeeUiState.collect {
                    when (it) {
                        is EmployeeViewModel.EmployeeUiState.Success -> updateData(it.employees)
                        is EmployeeViewModel.EmployeeUiState.Error -> showError(it.exception)
                    }
                }
            }
        }

        employeeViewModel.openEmployeeDetailsEvent.observe(viewLifecycleOwner) { employee ->
            employee?.let {
                //DODO: use NavController
                (activity as EmployeeListActivity).showEmployee(it)
                //findNavController().navigate(R.id.fragment_container)
            }
        }
    }

    private fun showError(exception: Throwable) {
        Log.d("TAG","in On Error ${exception.message}")
        view?.let { Snackbar.make(it, "On Error ${exception.message}", Snackbar.LENGTH_LONG).show() }
    }

    private fun updateData(list: List<EmployeeModel>) {
        Log.d("TAG","in updateData list = ${list.size}")
        mItemAnimation.animation.reset()
        employeeList.layoutAnimation = mItemAnimation
        employeeListAdapter.setEmployeeList(list.reversed())
        employeeList.scheduleLayoutAnimation()
    }

    private fun initRecycler() {
        mItemAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
    }

    companion object {
        val TAG = "EmployeeListFragment"
    }
}