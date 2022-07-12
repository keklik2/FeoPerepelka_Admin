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
): BaseViewModel(app) {

    private val _categoriesListLoading = OrdinaryLoading(
        viewModelScope,
        load = { Server.getAllCategories() }
    )
    val categoriesListState by stateFromFlow(_categoriesListLoading.stateFlow)

    fun goToDetailCategoryScreen() = router.navigateTo(Screens.CategoryDetailActivity())
    fun goToDetailCategoryScreen(category: CategoryModel) = router.navigateTo(Screens.CategoryDetailActivity(category))

    fun refreshData() {
        withScope {
            _categoriesListLoading.refresh()
        }
    }

}
