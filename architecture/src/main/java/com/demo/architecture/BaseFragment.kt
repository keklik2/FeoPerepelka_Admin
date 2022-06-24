package com.demo.architecture

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.demo.architecture.dialogs.AppAlertDialog
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPresser()
    }

    override fun onResume() {
        super.onResume()

        setupListeners?.invoke()
        setupBinds?.invoke()

        vm.showAlert bind {
            AppAlertDialog(it).show(childFragmentManager, DIALOG_TAG)
        }
        vm.showDatePicker bind {
            DatePickerDialog(
                requireContext(),
                it.onDateSetListener,
                it.calendar.get(Calendar.YEAR),
                it.calendar.get(Calendar.MONTH),
                it.calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    companion object {
        private const val DIALOG_TAG = "DialogTag"
        private const val TAG = "vmTag"
    }


}
