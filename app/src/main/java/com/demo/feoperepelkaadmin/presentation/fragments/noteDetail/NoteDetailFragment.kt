package com.demo.feoperepelkaadmin.presentation.fragments.noteDetail

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.doubleToStr
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentNoteDetailBinding
import com.demo.feoperepelkaadmin.server.models.ProductModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : BaseFragment(R.layout.fragment_note_detail) {
    override val binding: FragmentNoteDetailBinding by viewBinding()
    override val vm: NoteDetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAcceptBtnListener()
    }
    override var setupBinds: (() -> Unit)? = {
        bindCategories()

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
                    tvImageUrl.text = it.imgTitle
                    tietDescription.setText(it.description)
                    tietWeight.setText(doubleToStr(it.weight))
                    tietPrice.setText(doubleToStr(it.price))
                    spinnerStatus.setSelection(
                        if (it.enabled) 0
                        else 1
                    )
                }
            }
        }
    }

    private fun bindCategories() {
        vm::categories bind {
            if (it.isNullOrEmpty()) {
                binding.spinnerCategory.adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        mutableListOf("- none -")
                    )
                binding.spinnerCategory.setSelection(0)
            } else {
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    it
                )
                binding.spinnerCategory.adapter = adapter

                if (vm.note != null) {
                    val pos = adapter.getPosition(vm.note!!.category)
                    binding.spinnerCategory.setSelection(pos)
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
                binding.spinnerStatus.selectedItemPosition == 0,
                binding.tietDescription.text.toString(),
                binding.tietWeight.text.toString(),
                binding.tietPrice.text.toString(),
                binding.tvImageUrl.text.toString(),
                vm.note!!.img // TODO("Add bitmap convert")
            )
            vm.exit()
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
