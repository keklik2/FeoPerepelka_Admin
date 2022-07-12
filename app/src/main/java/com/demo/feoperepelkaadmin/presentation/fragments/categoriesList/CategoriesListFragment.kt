package com.demo.feoperepelkaadmin.presentation.fragments.categoriesList

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentCategoriesListBinding
import dagger.hilt.android.AndroidEntryPoint
import me.aartikov.sesame.loading.simple.Loading

@AndroidEntryPoint
class CategoriesListFragment : BaseFragment(R.layout.fragment_categories_list) {
    override val binding: FragmentCategoriesListBinding by viewBinding()
    override val vm: CategoriesListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAddCategoryBtnListener()
        setupRecyclerScrollListener()
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

        vm::categoriesListState bind {
            when(it) {
                is Loading.State.Data -> {
                    it.data.observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                        showFb()
                        hideLoadingBar()
                    }
                }
                is Loading.State.Loading -> {
                    showLoadingBar()
                    hideFb()
                }
                else -> {
                    hideLoadingBar()
                    showFb()
                    adapter.submitList(emptyList())
                }
            }
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

    private fun setupRecyclerScrollListener() {
        binding.rvCategories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) if (isFbVisible()) hideFb()
                if (dy < 0) if (!isFbVisible()) showFb()
            }
        })
    }


    /**
     * Helper functions
     */
    private fun showLoadingBar() = binding.loadingBar.setVisibility(true)
    private fun hideLoadingBar() = binding.loadingBar.setVisibility(false)
    private fun showFb() = binding.fbAddCategory.show()
    private fun hideFb() = binding.fbAddCategory.hide()
    private fun isFbVisible(): Boolean = binding.fbAddCategory.isVisible


    /**
     * Overridden functions
     */
    override fun onResume() {
        super.onResume()
        vm.refreshData()
    }
}
