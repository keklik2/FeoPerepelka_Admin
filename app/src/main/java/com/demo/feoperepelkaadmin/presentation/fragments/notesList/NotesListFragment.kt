package com.demo.feoperepelkaadmin.presentation.fragments.notesList

import android.graphics.BitmapFactory
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.setVisibility
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
        setupProductsBind()
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
     * Binds
     */
    private fun setupProductsBind() {
        binding.rvNotes.adapter = adapter
        vm::prodList bind {
            adapter.submitList(it)
            binding.tvEmptyNotes.setVisibility(it.isEmpty())
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
