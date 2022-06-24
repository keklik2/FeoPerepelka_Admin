package com.demo.feoperepelkaadmin.presentation.fragments.ordersList

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
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
            vm.goToDetailOrder(OrderModel(
                "Title",
                mutableMapOf(
                    Pair("Test", 2),
                    Pair("Test 1", 999),
                    Pair("Test 2", 1001),
                    Pair("Test 3", 1999),
                    Pair("Test 4", 9999),
                    Pair("Test 5", 10000000)
                ),
                "Customer",
                "Address",
                "Description",
                "PhoneNumber",
                Date().time
            ))
        }
    }

    private fun goToCall(phoneNumber: String) {

    }
}
