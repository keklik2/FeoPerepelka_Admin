package com.demo.feoperepelkaadmin.presentation.fragments.noInternet

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentNoInternetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoInternetFragment: BaseFragment(R.layout.fragment_no_internet) {
    override val binding: FragmentNoInternetBinding by viewBinding()
    override val vm: NoInternetViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {

    }
    override var setupBinds: (() -> Unit)? = {

    }
}
