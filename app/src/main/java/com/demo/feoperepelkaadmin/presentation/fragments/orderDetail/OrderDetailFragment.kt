package com.demo.feoperepelkaadmin.presentation.fragments.orderDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.dateToStrForDisplay
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentOrderDetailBinding
import com.demo.feoperepelkaadmin.server.models.OrderModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class OrderDetailFragment: BaseFragment(R.layout.fragment_order_detail) {
    override val binding: FragmentOrderDetailBinding by viewBinding()
    override val vm: OrderDetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAcceptBtnListener()
        setupCopyBtnListener()
        setupDeleteBtnListener()
        setupPhoneBtnListener()
    }
    override var setupBinds: (() -> Unit)? = {
        bindOrder()
        bindProducts()
    }

    /**
     * Individual variables
     */
    private val adapter by lazy {
        ProductAdapter.get {
            // vm.updateProductItem()
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
            Log.d("vmtest", "size: ${it.size}")
            Log.d("vmtest", "orders size: ${vm.order!!.shopList.size}")
        }
    }

    /**
     * Listeners
     */
    private fun setupPhoneBtnListener() {
        binding.buttonCallNumber.setOnClickListener {
            // TODO("Open phone call activity")
        }
    }

    private fun setupCopyBtnListener() {
        binding.buttonCopy.setOnClickListener { vm.getOrderAsCopiedText() }
    }

    private fun setupDeleteBtnListener() {
        binding.buttonDeleteOrder.setOnClickListener { vm.deleteOrder() }
    }

    private fun setupAcceptBtnListener() {
        binding.buttonAccept.setOnClickListener {
            vm.saveItem(binding.tietAddress.text.toString())
            vm.exit()
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
