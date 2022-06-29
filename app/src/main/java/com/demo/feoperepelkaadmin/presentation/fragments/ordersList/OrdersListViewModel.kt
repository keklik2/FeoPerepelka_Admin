package com.demo.feoperepelkaadmin.presentation.fragments.ordersList

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class OrdersListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
): BaseViewModel(app) {

    var ordersList: MutableList<OrderModel> by state(mutableListOf())

    /**
     * Navigation functions
     */
    fun goToDetailOrder(order: OrderModel) = router.navigateTo(Screens.OrderDetailActivity(order))

    init {
        getAllOrders()
    }

    private fun getAllOrders() {
        Server.getAllOrders(
            { ordersList = it.toMutableList() },
            {
                AppDialogContainer(
                    title = getString(R.string.dialog_error),
                    message = it.toString(),
                    positiveBtnCallback = {  },
                )
            }
        )
    }

    fun addOrder() {
        Server.addTestOrder{
            AppDialogContainer(
                title = getString(R.string.dialog_error),
                message = it.toString(),
                positiveBtnCallback = {  },
            )
        }
    }

}
