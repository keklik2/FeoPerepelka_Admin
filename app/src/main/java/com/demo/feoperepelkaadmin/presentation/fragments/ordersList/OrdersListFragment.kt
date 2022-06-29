package com.demo.feoperepelkaadmin.presentation.fragments.ordersList

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentOrdersListBinding
import com.demo.feoperepelkaadmin.server.models.OrderModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OrdersListFragment: BaseFragment(R.layout.fragment_orders_list) {
    override val binding: FragmentOrdersListBinding by viewBinding()
    override val vm: OrdersListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        addOrderBtnListener()
    }
    override var setupBinds: (() -> Unit)? = {
        setupAdapterBind()
    }

    /**
     * Individual variables
     */
    private val adapter by lazy {
        OrderAdapter.get(
            { vm.goToDetailOrder(it) },
            { goToCall(it) }
        )
    }


    /**
     * Listeners
     */
    private fun addOrderBtnListener() {
        binding.olfFbAddOrder.setOnClickListener {
            vm.addOrder()
        }
    }


    /**
     * Binds
     */
    private fun setupAdapterBind() {
        binding.olfRvOrders.adapter = adapter
        vm::ordersList bind {
            adapter.submitList(it)
            binding.olfTvEmptyOrders.setVisibility(it.isEmpty())
        }
    }


    /**
     * Additional functions
     */
    private fun goToCall(phoneNumber: String) {

    }
}
