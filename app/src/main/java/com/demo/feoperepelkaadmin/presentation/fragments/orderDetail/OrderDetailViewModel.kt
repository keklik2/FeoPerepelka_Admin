package com.demo.feoperepelkaadmin.presentation.fragments.orderDetail

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.helpers.refactorString
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.autorun
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    var order: OrderModel? by state(null)
    var products: MutableList<ProductItem> by state(mutableListOf())

    init {
        autorun(::order) { o ->
            o?.let { products = getProductItemsFromMap(it.shopList) }
        }
    }

    fun saveItem(address: String?) {
        val rAddress = refactorString(address)
        val rProducts = refactorProducts(products)
        Server.updateOrder(
            order!!.copy(
                address = rAddress,
                shopList = rProducts
            )
        ) {
            showAlert(
                AppDialogContainer(
                    title = getString(R.string.dialog_error),
                    message = it.toString(),
                    positiveBtnCallback = { }
                )
            )
        }
    }

    fun deleteOrder() {
        Server.deleteOrder(
            order!!
        ) {
            AppDialogContainer(
                title = getString(R.string.dialog_error),
                message = it.toString(),
                positiveBtnCallback = {  }
            )
        }
    }

    private fun refactorProducts(list: List<ProductItem>): MutableMap<String, Int> {
        val toReturn = mutableMapOf<String, Int>()
        for (i in list) { toReturn[i.title] = i.amount }
        return toReturn
    }

    private fun getProductItemsFromMap(map: Map<String, Int>): MutableList<ProductItem> {
        val toReturn = mutableListOf<ProductItem>()
        for (p in map) { toReturn.add(ProductItem(p.key, p.value)) }
        return toReturn
    }

    fun getOrderAsCopiedText(): String {
        order?.let {

        }
        return ""
    }

}
