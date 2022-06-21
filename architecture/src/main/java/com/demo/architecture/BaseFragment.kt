package com.demo.architecture

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.demo.architecture.dialogs.DialogResolver
import me.aartikov.sesame.property.PropertyObserver

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
    private var dialogService: DialogResolver? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        vm.dialog.showAlert bind {
//            if (dialogService == null) dialogService =
//                DialogResolver(requireContext(), childFragmentManager)
//            dialogService?.show(it.first, it.second)
//        }
    }

    override fun onResume() {
        super.onResume()

        setupListeners?.invoke()
        setupBinds?.invoke()
    }


}
