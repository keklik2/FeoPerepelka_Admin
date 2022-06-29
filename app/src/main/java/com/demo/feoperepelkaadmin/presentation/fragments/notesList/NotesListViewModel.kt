package com.demo.feoperepelkaadmin.presentation.fragments.notesList

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
): BaseViewModel(app) {

    var prodList: MutableList<ProductModel> by state(mutableListOf())

    fun goToAddNoteScreen() = router.navigateTo(Screens.NoteDetailActivity())
    fun goToEditNoteScreen(note: ProductModel) = router.navigateTo(Screens.NoteDetailActivity(note))

    init {
        getProducts()
    }

    private fun getProducts() {
        Server.getAllProducts(
            { prodList = it.toMutableList() },
            {
                showAlert(
                    AppDialogContainer(
                        title = getString(R.string.dialog_error),
                        message = it.toString(),
                        positiveBtnCallback = { getProducts() },
                        negativeBtnCallback = {  }
                    )
                )
            }
        )
    }

}
