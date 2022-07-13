package com.demo.feoperepelkaadmin.presentation.fragments.noteDetail

import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.demo.architecture.BaseFragment
import com.demo.architecture.files.PicturesPicker
import com.demo.architecture.helpers.doubleToStr
import com.demo.architecture.helpers.setVisibility
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
        setupPicturePickerListener()
        setupRestoreListener()
    }
    override var setupBinds: (() -> Unit)? = {
        bindCategories()
        bindFields()
        bindGetBtm()
        bindExit()
    }
    private val filePicker = PicturesPicker(this) {
        vm.imgUri = it
    }


    /**
     * Bindings
     */
    private fun bindFields() {
        vm::note bind { pm ->
            pm?.let {
                with(binding) {
                    tietNoteTitle.setText(it.title)
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
        vm::imgTitle bind {
            if(!it.isNullOrBlank() && it.isNotBlank()) binding.addImgBtn.text = it
        }
        vm::imgBtm bind {
            if (it != null) {
                Glide
                    .with(requireActivity())
                    .load(it)
                    .encodeQuality(60)
                    .centerCrop()
                    .into(binding.ivProduct)

                if (vm.note == null || (vm.note != null && vm.note!!.img != it)) binding.btnRestore.setVisibility(true)
                else binding.btnRestore.setVisibility(false)
            }
            else binding.btnRestore.setVisibility(false)
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

    private fun bindGetBtm() {
        vm.decodeBitmap bind {
            vm.imgBtm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireContext().contentResolver,
                        it
                    )
                )
            else MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
        }
    }

    private fun bindExit() {
        vm::canCloseScreen bind {
            if (it) vm.exit()
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
                binding.tietPrice.text.toString()
            )
        }
    }

    private fun setupPicturePickerListener() {
        binding.addImgBtn.setOnClickListener {
            filePicker.openPicturesPicker()
        }
    }

    private fun setupRestoreListener() {
        binding.btnRestore.setOnClickListener {
            vm.imgUri = null
            if (vm.note != null) {
                vm.imgBtm = vm.note!!.img
                vm.imgTitle = vm.note!!.imgTitle
            }
            else {
                vm.imgBtm = null
                vm.imgTitle = null
                binding.addImgBtn.text = getString(R.string.button_add_img)
                Glide
                    .with(requireActivity())
                    .load(R.drawable.ic_image_err_gray)
                    .into(binding.ivProduct)
            }
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

    override fun onResume() {
        super.onResume()
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
