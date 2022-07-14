package com.demo.feoperepelkaadmin.presentation.fragments.notesList

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentNotesListBinding
import dagger.hilt.android.AndroidEntryPoint
import me.aartikov.sesame.loading.simple.Loading

@AndroidEntryPoint
class NotesListFragment: BaseFragment(R.layout.fragment_notes_list) {
    override val binding: FragmentNotesListBinding by viewBinding()
    override val vm: NotesListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAddNoteBtnListener()
        setupRecyclerScrollListener()
        setupRecyclerOnSwipeListener()
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

        vm::productsListState bind {
            when(it) {
                is Loading.State.Data -> {
                    hideEmptyNotes()
                    it.data.observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                        hideLoadingBar()
                        showFb()
                        if (list.isEmpty()) showEmptyNotes()
                    }
                }
                is Loading.State.Loading -> {
                    hideFb()
                    hideEmptyNotes()
                    showLoadingBar()
                }
                else -> {
                    showFb()
                    hideLoadingBar()
                    showEmptyNotes()
                    adapter.submitList(emptyList())
                }
            }
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

    private fun setupRecyclerScrollListener() {
        binding.rvNotes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) if (isFbVisible()) hideFb()
                if (dy < 0) if (!isFbVisible()) showFb()
            }
        })
    }

    private fun setupRecyclerOnSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currItem =
                    adapter.currentList[viewHolder.absoluteAdapterPosition]
                binding.rvNotes.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                vm.deleteNote(currItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvNotes)
    }




    /**
     * Helper functions
     */
    private fun showLoadingBar() = binding.loadingBar.setVisibility(true)
    private fun hideLoadingBar() = binding.loadingBar.setVisibility(false)
    private fun showEmptyNotes() = binding.tvEmptyNotes.setVisibility(true)
    private fun hideEmptyNotes() = binding.tvEmptyNotes.setVisibility(false)
    private fun showFb() = binding.fbAddNote.show()
    private fun hideFb() = binding.fbAddNote.hide()
    private fun isFbVisible(): Boolean = binding.fbAddNote.isVisible


    /**
     * Overridden functions
     */
    override fun onResume() {
        super.onResume()
        vm.refreshData()
    }
}
