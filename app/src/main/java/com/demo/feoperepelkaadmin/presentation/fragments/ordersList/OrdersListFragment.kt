package com.demo.feoperepelkaadmin.presentation.fragments.ordersList

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentOrdersListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersListFragment: BaseFragment(R.layout.fragment_orders_list) {
    override val binding: FragmentOrdersListBinding by viewBinding()
    override val vm: OrdersListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {

    }
    override var setupBinds: (() -> Unit)? = {

    }

    /**
     * Individual variables
     */
    private val adapter by lazy {
        OrderAdapter.get(
            {
                // vm.goToDetailOrder(it)
            },
            {
                // goToCall(it)
            }
        )
    }
}
