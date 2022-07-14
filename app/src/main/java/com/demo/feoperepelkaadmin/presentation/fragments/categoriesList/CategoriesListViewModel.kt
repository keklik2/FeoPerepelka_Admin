package com.demo.feoperepelkaadmin.presentation.fragments.categoriesList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.loading.simple.OrdinaryLoading
import me.aartikov.sesame.loading.simple.refresh
import me.aartikov.sesame.property.state
import me.aartikov.sesame.property.stateFromFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    private val _categoriesListLoading = OrdinaryLoading(
        viewModelScope,
        load = { Server.getAllCategories() }
    )
    val categoriesListState by stateFromFlow(_categoriesListLoading.stateFlow)

    fun goToDetailCategoryScreen() = router.navigateTo(Screens.CategoryDetailActivity())
    fun goToDetailCategoryScreen(category: CategoryModel) =
        router.navigateTo(Screens.CategoryDetailActivity(category))

    fun deleteCategory(category: CategoryModel) {
        if (!category.locked) {
            showAlert(AppDialogContainer(
                getString(R.string.dialog_title_category_delete),
                getString(R.string.dialog_category_delete),
                positiveBtnCallback = {
                    Server.deleteCategory(
                        category,
                        {
                            AppDialogContainer(
                                title = getString(R.string.dialog_title_error),
                                message = it.toString(),
                                positiveBtnCallback = { }
                            )
                        },
                        { refreshData() }
                    )
                },
                negativeBtnCallback = { }
            ))
        } else showAlert(
            AppDialogContainer(
                getString(R.string.dialog_title_category_locked),
                getString(R.string.dialog_category_locked),
                positiveBtnCallback = {}
            )
        )
    }

    fun refreshData() {
        withScope {
            _categoriesListLoading.refresh()
        }
    }

}
