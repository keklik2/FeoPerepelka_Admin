package com.demo.feoperepelkaadmin.presentation.fragments.categoryDetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentCategoryDetailBinding
import com.demo.feoperepelkaadmin.presentation.fragments.noteDetail.NoteDetailFragment
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryDetailFragment: BaseFragment(R.layout.fragment_category_detail) {
    override val binding: FragmentCategoryDetailBinding by viewBinding()
    override val vm: CategoryDetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAcceptBtnListener()
    }
    override var setupBinds: (() -> Unit)? = {
        setupCategoryBind()
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


    /**
     * Listeners
     */
    private fun setupAcceptBtnListener() {
        binding.buttonAccept.setOnClickListener {
            vm.saveItem(binding.tietCategoryTitle.text.toString())
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
