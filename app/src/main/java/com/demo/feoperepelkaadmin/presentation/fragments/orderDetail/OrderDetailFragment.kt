package com.demo.feoperepelkaadmin.presentation.fragments.orderDetail

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentOrderDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment: BaseFragment(R.layout.fragment_order_detail) {
    override val binding: FragmentOrderDetailBinding by viewBinding()
    override val vm: OrderDetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {

    }
    override var setupBinds: (() -> Unit)? = {

    }

    /**
     * Individual variables
     */
    private val adapter by lazy {
        ProductAdapter.get {
            // vm.updateProductItem()
        }
    }
}
