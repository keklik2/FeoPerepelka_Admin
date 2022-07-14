package com.demo.feoperepelkaadmin.presentation.fragments.noteDetail

import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
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
import io.github.anderscheow.validator.Validator
import io.github.anderscheow.validator.rules.common.NotBlankRule
import io.github.anderscheow.validator.rules.common.NotEmptyRule
import io.github.anderscheow.validator.validation
import io.github.anderscheow.validator.validator

@AndroidEntryPoint
class NoteDetailFragment : BaseFragment(R.layout.fragment_note_detail) {
    override val binding: FragmentNoteDetailBinding by viewBinding()
    override val vm: NoteDetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAcceptBtnListener()
        setupPicturePickerListener()
        setupRestoreListener()
        setupValidationListeners()
    }
    override var setupBinds: (() -> Unit)? = {
        bindCategories()
        bindFields()
        bindGetBtm()
        bindSwitchLoading()
        bindExit()
    }
    private val filePicker = PicturesPicker(this) {
        vm.imgUri = it
    }


    /**
     * Validations
     */
    private val titleValidation by lazy {
        validation(binding.tilNoteTitle) {
            rules {
                +NotEmptyRule(ERR_EMPTY_TITLE)
                +NotBlankRule(ERR_BLANK_TITLE)
            }
        }
    }

    private val descriptionValidation by lazy {
        validation(binding.tilDescription) {
            rules {
                +NotEmptyRule(ERR_EMPTY_DESCRIPTION)
                +NotBlankRule(ERR_BLANK_DESCRIPTION)
            }
        }
    }

    private val weightValidation by lazy {
        validation(binding.tilWeight) {
            rules {
                +NotEmptyRule(ERR_EMPTY_WEIGHT)
                +NotBlankRule(ERR_BLANK_WEIGHT)
            }
        }
    }

    private val priceValidation by lazy {
        validation(binding.tilPrice) {
            rules {
                +NotEmptyRule(ERR_EMPTY_PRICE)
                +NotBlankRule(ERR_BLANK_PRICE)
            }
        }
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
            if (!it.isNullOrBlank() && it.isNotBlank()) binding.addImgBtn.text = it
        }
        vm::imgBtm bind {
            if (it != null) {
                Glide
                    .with(requireActivity())
                    .load(it)
                    .encodeQuality(60)
                    .centerCrop()
                    .into(binding.ivProduct)

                if (vm.note == null || (vm.note != null && vm.note!!.img != it)) binding.btnRestore.setVisibility(
                    true
                )
                else binding.btnRestore.setVisibility(false)
            } else binding.btnRestore.setVisibility(false)
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

    private fun bindSwitchLoading() {
        vm.switchLoading bind {
            binding.layoutLoading.setVisibility(it)
        }
    }

    private fun bindExit() = vm::canCloseScreen bind { if (it) vm.exit() }


    /**
     * Listeners
     */
    private fun setupAcceptBtnListener() {
        binding.buttonAccept.setOnClickListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {
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
                validate(titleValidation, descriptionValidation, weightValidation, priceValidation)
            }
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
            } else {
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

    private fun setupValidationListeners() {
        binding.tietNoteTitle.addTextChangedListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {}
                }
                validate(titleValidation)
            }
        }

        binding.tietDescription.addTextChangedListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {}
                }
                validate(descriptionValidation)
            }
        }

        binding.tietWeight.addTextChangedListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {}
                }
                validate(weightValidation)
            }
        }

        binding.tietPrice.addTextChangedListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {}
                }
                validate(priceValidation)
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

    companion object {
        private const val ERR_BLANK_TITLE = R.string.item_title_error
        private const val ERR_EMPTY_TITLE = R.string.item_title_error

        private const val ERR_BLANK_DESCRIPTION = R.string.note_description_error
        private const val ERR_EMPTY_DESCRIPTION = R.string.note_description_error

        private const val ERR_BLANK_WEIGHT = R.string.note_weight_error
        private const val ERR_EMPTY_WEIGHT = R.string.note_weight_error

        private const val ERR_BLANK_PRICE = R.string.note_price_error
        private const val ERR_EMPTY_PRICE = R.string.note_price_error

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
