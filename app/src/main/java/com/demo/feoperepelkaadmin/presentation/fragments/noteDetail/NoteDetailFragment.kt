package com.demo.feoperepelkaadmin.presentation.fragments.noteDetail

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.doubleToStr
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentNoteDetailBinding
import com.demo.feoperepelkaadmin.server.models.ProductModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment: BaseFragment(R.layout.fragment_note_detail) {
    override val binding: FragmentNoteDetailBinding by viewBinding()
    override val vm: NoteDetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAcceptBtnListener()
    }
    override var setupBinds: (() -> Unit)? = {
        bindFields()
    }


    /**
     * Bindings
     */
    private fun bindFields() {
        vm::note bind { pm ->
            pm?.let {
                with(binding) {
                    tietNoteTitle.setText(it.title)
                    // TODO ("Set category choosing from existing. If doesn't exist, add category to list for spinner & set it")
                    tvImageUrl.text = it.imgTitle
                    tietDescription.setText(it.description)
                    tietWeight.setText(doubleToStr(it.weight))
                    spinnerStatus.setSelection(
                        if (it.enabled) 0
                        else 1
                    )
                }
            }
        }
    }


    /**
     * Listeners
     */
    private fun setupAcceptBtnListener() {
        binding.buttonAccept.setOnClickListener {
            vm.saveItem(
                binding.tietNoteTitle.text.toString(),
                binding.spinnerCategory.selectedItem as String,
                true, //TODO("Get boolean from selected spinner item")
                binding.tietDescription.text.toString(),
                binding.tietWeight.text.toString(),
                binding.tietPrice.text.toString(),
                binding.tvImageUrl.text.toString(),
                Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888) // TODO("Add bitmap convert")
            )
        }
    }


    /**
     * Base functions
     */
    private fun getArgs() {
        if (arguments != null) {
            with(requireArguments()) {
                if (containsKey(NOTE_KEY)) vm.note = getParcelable(NOTE_KEY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    companion object {
        private const val NOTE_KEY = "note_key"

        fun newInstance(): NoteDetailFragment = NoteDetailFragment()

        fun newInstance(note: ProductModel): NoteDetailFragment {
            return NoteDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(NOTE_KEY, note)
                }
            }
        }

    }
}
