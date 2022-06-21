package com.demo.feoperepelkaadmin.presentation.fragments.login

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseFragment(R.layout.fragment_login) {
    override val binding: FragmentLoginBinding by viewBinding()
    override val vm: LoginViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {

    }
    override var setupBinds: (() -> Unit)? = {

    }
}
