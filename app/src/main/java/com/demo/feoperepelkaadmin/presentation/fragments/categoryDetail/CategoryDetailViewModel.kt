package com.demo.feoperepelkaadmin.presentation.fragments.categoryDetail

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.helpers.refactorString
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    var category: CategoryModel? by state(null)

    fun saveItem(title: String?) {
        val rTitle = refactorString(title)

        Server.addOrUpdateCategory(
            if (category != null) category!!.copy(title = rTitle)
            else CategoryModel(rTitle, LOCKED),
            onErrorCallback = {
                showAlert(
                    AppDialogContainer(
                        title = getString(R.string.dialog_title_error),
                        message = it.toString(),
                        positiveBtnCallback = { }
                    )
                )
            },
            onSuccessCallback = { setCanCloseScreen() }
        )
    }

    companion object {
        private const val LOCKED = false
    }


    /**
     * Fragment exit observer
     */
    var canCloseScreen by state(false)
    private fun setCanCloseScreen() {
        canCloseScreen = true
    }
}
