package com.demo.feoperepelkaadmin.presentation.fragments.ordersList

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
): BaseViewModel(app) {

    fun goToDetailOrder(order: OrderModel) = router.navigateTo(Screens.OrderDatail(order))

}
