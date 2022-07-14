package com.demo.feoperepelkaadmin.presentation.fragments.orderDetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.dateToStrForDisplay
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentOrderDetailBinding
import com.demo.feoperepelkaadmin.server.models.OrderModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.anderscheow.validator.Validator
import io.github.anderscheow.validator.rules.common.NotBlankRule
import io.github.anderscheow.validator.rules.common.NotEmptyRule
import io.github.anderscheow.validator.validation
import io.github.anderscheow.validator.validator

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment(R.layout.fragment_order_detail) {
    override val binding: FragmentOrderDetailBinding by viewBinding()
    override val vm: OrderDetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAcceptBtnListener()
        setupCopyBtnListener()
        setupDeleteBtnListener()
        setupPhoneBtnListener()
        setupAddressChangedListener()
    }
    override var setupBinds: (() -> Unit)? = {
        bindOrder()
        bindProducts()
        bindSwitchLoading()
        bindExit()
    }

    /**
     * Individual variables
     */
    private val adapter by lazy {
        ProductAdapter.get { vm.checkProductsAmount(it) }
    }


    /**
     * Validation
     */
    private val addressValidation by lazy {
        validation(binding.tilAddress) {
            rules {
                +NotEmptyRule(ERR_EMPTY_ADDRESS)
                +NotBlankRule(ERR_BLANK_ADDRESS)
            }
        }
    }


    /**
     * Bindings
     */
    private fun bindOrder() {
        vm::order bind { om ->
            binding.rvProducts.adapter = adapter
            om?.let {
                with(binding) {
                    tvTitle.text = it.title
                    tietAddress.setText(it.address)
                    tvClient.text = it.customer
                    tvDescription.text = it.description
                    tvDate.text = dateToStrForDisplay(it.date)
                    buttonCallNumber.text = it.phoneNumber
                }
            }
        }
    }

    private fun bindProducts() {
        binding.rvProducts.adapter = adapter
        vm::products bind {
            adapter.submitList(it)
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
    private fun setupPhoneBtnListener() {
        binding.buttonCallNumber.setOnClickListener {
            vm.order?.let {
                startActivity(
                    Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel: ${it.phoneNumber}")
                    }
                )
            }
        }
    }

    private fun setupCopyBtnListener() {
        binding.buttonCopy.setOnClickListener {
            (requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
                setPrimaryClip(
                    ClipData.newPlainText(
                        OrderModel.TITLE_KEY,
                        vm.getOrderAsCopiedText()
                    )
                )
            }
            makeToast(getString(TEXT_COPIED_TOAST))
        }
    }

    private fun setupDeleteBtnListener() {
        binding.buttonDeleteOrder.setOnClickListener { vm.deleteOrder() }
    }

    private fun setupAcceptBtnListener() {
        binding.buttonAccept.setOnClickListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {
                        vm.saveItem(binding.tietAddress.text.toString())
                    }
                }
                validate(addressValidation)
            }
        }
    }

    private fun setupAddressChangedListener() {
        binding.tietAddress.addTextChangedListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {}
                }
                validate(addressValidation)
            }
        }
    }


    /**
     * Base functions
     */
    private fun getArgs() {
        with(requireArguments()) {
            if (containsKey(ORDER_KEY)) vm.order = getParcelable(ORDER_KEY)
            else throw Exception("OrderDetailFragment must receive Order Model with .newInstance()")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    companion object {
        private const val ERR_BLANK_ADDRESS = R.string.order_address_error
        private const val ERR_EMPTY_ADDRESS = R.string.order_address_error

        private const val TEXT_COPIED_TOAST = R.string.toast_msg_copied

        private const val ORDER_KEY = "order_key"

        fun newInstance(order: OrderModel): OrderDetailFragment {
            return OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ORDER_KEY, order)
                }
            }
        }
    }
}
