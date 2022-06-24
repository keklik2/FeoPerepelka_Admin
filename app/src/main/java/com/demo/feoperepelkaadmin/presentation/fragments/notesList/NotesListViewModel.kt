package com.demo.feoperepelkaadmin.presentation.fragments.notesList

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
): BaseViewModel(app) {

    fun goToAddNoteScreen() = router.navigateTo(Screens.NoteDatail())
    fun goToEditNoteScreen(note: ProductModel) = router.navigateTo(Screens.NoteDatail(note))

}
