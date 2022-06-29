package com.demo.feoperepelkaadmin.presentation.fragments.categoriesList

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
): BaseViewModel(app) {

    var categoriesList: MutableList<CategoryModel>? by state(null)

    fun goToDetailCategoryScreen() = router.navigateTo(Screens.CategoryDetailActivity())
    fun goToDetailCategoryScreen(category: CategoryModel) = router.navigateTo(Screens.CategoryDetailActivity(category))

    private fun getCategories() {
        Server.getAllCategories(
            { categoriesList = it.toMutableList() },
            {
                showAlert(
                    AppDialogContainer(
                        title = getString(R.string.dialog_error),
                        message = it.toString(),
                        positiveBtnCallback = { getCategories() },
                        negativeBtnCallback = {  }
                    )
                )
            }
        )
    }

    fun updateCategoriesList() {
        getCategories()
    }
}
