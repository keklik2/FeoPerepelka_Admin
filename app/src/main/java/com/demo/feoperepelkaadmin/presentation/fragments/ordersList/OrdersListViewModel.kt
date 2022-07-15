package com.demo.feoperepelkaadmin.presentation.fragments.ordersList

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.loading.simple.OrdinaryLoading
import me.aartikov.sesame.loading.simple.refresh
import me.aartikov.sesame.property.state
import me.aartikov.sesame.property.stateFromFlow
import javax.inject.Inject

@HiltViewModel
class OrdersListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    private val _ordersListLoading = OrdinaryLoading(
        viewModelScope,
        load = { Server.getAllOrders() }
    )
    val ordersListState by stateFromFlow(_ordersListLoading.stateFlow)
    fun goToDetailOrder(order: OrderModel) = router.navigateTo(Screens.OrderDetailActivity(order))

    fun addOrder() {
        Server.addTestOrder(
            {
                showAlert(
                    AppDialogContainer(
                        title = getString(R.string.dialog_title_error),
                        message = it.toString(),
                        positiveBtnCallback = { },
                    )
                )
            },
            { refreshData() }
        )
    }

    fun refreshData() {
        withScope {
            _ordersListLoading.refresh()
        }
    }

}
