package com.demo.feoperepelkaadmin.presentation.fragments.login

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentLoginBinding
import com.demo.feoperepelkaadmin.presentation.fragments.categoryDetail.CategoryDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.anderscheow.validator.Validator
import io.github.anderscheow.validator.rules.common.NotBlankRule
import io.github.anderscheow.validator.rules.common.NotEmptyRule
import io.github.anderscheow.validator.validation
import io.github.anderscheow.validator.validator

@AndroidEntryPoint
class LoginFragment: BaseFragment(R.layout.fragment_login) {
    override val binding: FragmentLoginBinding by viewBinding()
    override val vm: LoginViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupLoginBtnListener()
        setupLoginChangedListener()
        setupPasswordChangedListener()
    }
    override var setupBinds: (() -> Unit)? = {
        bindSwitchLoading()
    }


    /**
     * Binds
     */
    private fun bindSwitchLoading() {
        vm.switchLoading bind {
            binding.layoutLoading.setVisibility(it)
        }
    }


    /**
     * Validation
     */
    private val loginValidation by lazy {
        validation(binding.tilLogin) {
            rules {
                +NotEmptyRule(ERR_EMPTY_LOGIN)
                +NotBlankRule(ERR_BLANK_LOGIN)
            }
        }
    }

    private val passwordValidation by lazy {
        validation(binding.tilLogin) {
            rules {
                +NotEmptyRule(ERR_EMPTY_PASSWORD)
                +NotBlankRule(ERR_BLANK_PASSWORD)
            }
        }
    }


    /**
     * Listeners
     */
    private fun setupLoginBtnListener() {
        binding.buttonLogin.setOnClickListener {
            vm.tryLogin(
                binding.tietLogin.text.toString(),
                binding.tietPassword.text.toString()
            )
        }
    }

    private fun setupLoginChangedListener() {
        binding.tietLogin.addTextChangedListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {}
                }
                validate(loginValidation)
            }
        }
    }

    private fun setupPasswordChangedListener() {
        binding.tietPassword.addTextChangedListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {}
                }
                validate(passwordValidation)
            }
        }
    }


    companion object {
        private const val ERR_BLANK_LOGIN = R.string.login_error
        private const val ERR_EMPTY_LOGIN = R.string.login_error
        private const val ERR_BLANK_PASSWORD = R.string.password_error
        private const val ERR_EMPTY_PASSWORD = R.string.password_error
    }
}
