package com.demo.feoperepelkaadmin.presentation.fragments.notesList

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentNotesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesListFragment: BaseFragment(R.layout.fragment_notes_list) {
    override val binding: FragmentNotesListBinding by viewBinding()
    override val vm: NotesListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAddNoteBtnListener()
    }
    override var setupBinds: (() -> Unit)? = {

    }

    /**
     * Individual variables
     */
    private val adapter by lazy {
        NoteAdapter.get {
             vm.goToEditNoteScreen(it)
        }
    }

    /**
     * Listeners
     */
    private fun setupAddNoteBtnListener() {
        binding.fbAddNote.setOnClickListener {
            vm.goToAddNoteScreen()
        }
    }
}
