package com.demo.feoperepelkaadmin.presentation.fragments.categoryDetail

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentCategoryDetailBinding
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.anderscheow.validator.Validator
import io.github.anderscheow.validator.rules.common.NotBlankRule
import io.github.anderscheow.validator.rules.common.NotEmptyRule
import io.github.anderscheow.validator.validation
import io.github.anderscheow.validator.validator

@AndroidEntryPoint
class CategoryDetailFragment: BaseFragment(R.layout.fragment_category_detail) {
    override val binding: FragmentCategoryDetailBinding by viewBinding()
    override val vm: CategoryDetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAcceptBtnListener()
        setupTitleChangedListener()
    }
    override var setupBinds: (() -> Unit)? = {
        setupCategoryBind()
        bindExit()
    }


    /**
     * Validation
     */
    private val titleValidation by lazy {
        validation(binding.tilCategoryTitle) {
            rules {
                +NotEmptyRule(ERR_EMPTY_TITLE)
                +NotBlankRule(ERR_BLANK_TITLE)
            }
        }
    }


    /**
     * Binds
     */
    private fun setupCategoryBind() {
        vm::category bind {
            it?.let {
                binding.tietCategoryTitle.setText(it.title)
            }
        }
    }

    private fun bindExit() {
        vm::canCloseScreen bind {
            if (it) vm.exit()
        }
    }


    /**
     * Listeners
     */
    private fun setupAcceptBtnListener() {
        binding.buttonAccept.setOnClickListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {
                        vm.saveItem(binding.tietCategoryTitle.text.toString())
                    }
                }
                validate(titleValidation)
            }
        }
    }

    private fun setupTitleChangedListener() {
        binding.tietCategoryTitle.addTextChangedListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {}
                }
                validate(titleValidation)
            }
        }
    }


    /**
    * Base functions
     */
    private fun getArgs() {
        if (arguments != null) {
            with(requireArguments()) {
                if (containsKey(CATEGORY_KEY)) vm.category = getParcelable(CATEGORY_KEY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    companion object {
        private const val ERR_BLANK_TITLE = R.string.item_title_error
        private const val ERR_EMPTY_TITLE = R.string.item_title_error

        private const val CATEGORY_KEY = "category_key"

        fun newInstance(): CategoryDetailFragment = CategoryDetailFragment()

        fun newInstance(category: CategoryModel): CategoryDetailFragment {
            return CategoryDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY_KEY, category)
                }
            }
        }

    }
}
