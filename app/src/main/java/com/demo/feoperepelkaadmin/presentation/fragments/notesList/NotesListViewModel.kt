package com.demo.feoperepelkaadmin.presentation.fragments.notesList

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.loading.simple.OrdinaryLoading
import me.aartikov.sesame.loading.simple.refresh
import me.aartikov.sesame.property.state
import me.aartikov.sesame.property.stateFromFlow
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
): BaseViewModel(app) {

    private val _productsListLoading = OrdinaryLoading(
        viewModelScope,
        load = { Server.getAllProducts() }
    )
    val productsListState by stateFromFlow(_productsListLoading.stateFlow)

    fun goToAddNoteScreen() = router.navigateTo(Screens.NoteDetailActivity())
    fun goToEditNoteScreen(note: ProductModel) = router.navigateTo(Screens.NoteDetailActivity(note))

    fun deleteNote(product: ProductModel) {
        showAlert(AppDialogContainer(
            getString(R.string.dialog_title_note_delete),
            getString(R.string.dialog_note_delete),
            positiveBtnCallback = {
                Server.deleteProduct(
                    product,
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
            negativeBtnCallback = {  }
        ))
    }

    fun refreshData() {
        withScope {
            _productsListLoading.refresh()
        }
    }
}
