package com.demo.feoperepelkaadmin.presentation.fragments.categoriesList

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentCategoriesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesListFragment : BaseFragment(R.layout.fragment_categories_list) {
    override val binding: FragmentCategoriesListBinding by viewBinding()
    override val vm: CategoriesListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAddCategoryBtnListener()
    }
    override var setupBinds: (() -> Unit)? = {
        setupAdapterBind()
    }

    /**
     * Individual variables
     */
    private val adapter by lazy {
        CategoryAdapter.get { vm.goToDetailCategoryScreen(it) }
    }


    /**
     * Binds
     */
    private fun setupAdapterBind() {
        binding.rvCategories.adapter = adapter
        vm::categoriesList bind {
            adapter.submitList(it)
        }
    }


    /**
     * Listeners
     */
    private fun setupAddCategoryBtnListener() {
        binding.fbAddCategory.setOnClickListener {
            vm.goToDetailCategoryScreen()
        }
    }


    /**
     * Overriden functions
     */
    override fun onResume() {
        super.onResume()
        vm.updateCategoriesList()
    }
}
