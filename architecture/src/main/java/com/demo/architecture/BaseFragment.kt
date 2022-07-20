package com.demo.architecture

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.demo.architecture.dialogs.AppAlertDialog
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.dialogs.AppListDialogContainer
import com.demo.architecture.dialogs.AppSingleChoiceListDialog
import me.aartikov.sesame.property.PropertyObserver
import java.util.*

abstract class BaseFragment(@LayoutRes layoutRes: Int): Fragment(layoutRes), PropertyObserver {

    override val propertyObserverLifecycleOwner: LifecycleOwner
        get() = viewLifecycleOwner

    /**
     * Methods & variables that must be override
     */
    abstract val binding: ViewBinding
    abstract val vm: BaseViewModel
    abstract var setupListeners: (() -> Unit)?
    abstract var setupBinds: (() -> Unit)?

    fun setupBackPresser() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                vm.exit()
            }
        })
    }

    fun makeToast(message: String, isLong: Boolean = false) {
        Toast.makeText(requireActivity(), message, if(isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }

    fun makeAlert(container: AppDialogContainer) {
        AppAlertDialog(container).show(childFragmentManager, DIALOG_TAG)
    }

    fun makeListDialog(container: AppListDialogContainer) {
        AppSingleChoiceListDialog(container).show(childFragmentManager, DIALOG_TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPresser()

        setupListeners?.invoke()
        setupBinds?.invoke()

        vm.showAlert bind { makeAlert(it) }
        vm.showDatePicker bind {
            DatePickerDialog(
                requireContext(),
                it.onDateSetListener,
                it.calendar.get(Calendar.YEAR),
                it.calendar.get(Calendar.MONTH),
                it.calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        vm.showToast bind { makeToast(it) }
        vm.showToastLong bind { makeToast(it, true) }
        vm.showListDialog bind { makeListDialog(it) }
    }

    companion object {
        private const val DIALOG_TAG = "DialogTag"
        private const val TAG = "vmTag"
    }


}
