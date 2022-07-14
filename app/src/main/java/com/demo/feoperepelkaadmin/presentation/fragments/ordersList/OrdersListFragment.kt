package com.demo.feoperepelkaadmin.presentation.fragments.ordersList

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.FragmentOrdersListBinding
import com.demo.feoperepelkaadmin.presentation.fragments.orderDetail.OrderDetailFragment
import com.demo.feoperepelkaadmin.server.models.OrderModel
import dagger.hilt.android.AndroidEntryPoint
import me.aartikov.sesame.loading.simple.Loading

@AndroidEntryPoint
class OrdersListFragment : BaseFragment(R.layout.fragment_orders_list) {
    override val binding: FragmentOrdersListBinding by viewBinding()
    override val vm: OrdersListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        addOrderBtnListener()
        setupRecyclerScrollListener()
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
            { goToCall(it) },
            { copyOrder(it) }
        )
    }


    /**
     * Listeners
     */
    private fun addOrderBtnListener() {
        binding.fbAddOrder.setOnClickListener {
            vm.addOrder()
        }
    }

    private fun setupRecyclerScrollListener() {
        binding.rvOrders.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) if (isFbVisible()) hideFb()
                if (dy < 0) if (!isFbVisible()) showFb()
            }
        })
    }


    /**
     * Binds
     */
    private fun setupAdapterBind() {
        binding.rvOrders.adapter = adapter

        vm::ordersListState bind {
            when (it) {
                is Loading.State.Data -> {
                    hideEmptyOrders()
                    it.data.observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                        hideLoadingBar()
                        showFb()
                        if (list.isEmpty()) showEmptyOrders()
                    }
                }
                is Loading.State.Loading -> {
                    hideFb()
                    hideEmptyOrders()
                    showLoadingBar()
                }
                else -> {
                    showFb()
                    hideLoadingBar()
                    showEmptyOrders()
                    adapter.submitList(emptyList())
                }
            }
        }
    }


    /**
     * Additional functions
     */
    private fun goToCall(phoneNumber: String) {
        startActivity(
            Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel: $phoneNumber")
            }
        )
    }

    private fun copyOrder(order: String) {
        (requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
            setPrimaryClip(
                ClipData.newPlainText(
                    OrderModel.TITLE_KEY,
                    order
                )
            )
        }
        makeToast(getString(TEXT_COPIED_TOAST))
    }

    /**
     * Helper functions
     */
    private fun showLoadingBar() = binding.loadingBar.setVisibility(true)
    private fun hideLoadingBar() = binding.loadingBar.setVisibility(false)
    private fun showEmptyOrders() = binding.tvEmptyOrders.setVisibility(true)
    private fun hideEmptyOrders() = binding.tvEmptyOrders.setVisibility(false)
    private fun showFb() = binding.fbAddOrder.show()
    private fun hideFb() = binding.fbAddOrder.hide()
    private fun isFbVisible(): Boolean = binding.fbAddOrder.isVisible


    /**
     * Overridden functions
     */
    override fun onResume() {
        super.onResume()
        vm.refreshData()
    }

    companion object {
        private const val TEXT_COPIED_TOAST = R.string.toast_msg_copied
    }
}
