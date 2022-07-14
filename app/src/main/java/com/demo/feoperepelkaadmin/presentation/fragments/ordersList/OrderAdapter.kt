package com.demo.feoperepelkaadmin.presentation.fragments.ordersList

import android.view.View
import com.demo.architecture.helpers.dateToStrForDisplay
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.ItemOrderBinding
import com.demo.feoperepelkaadmin.server.models.OrderModel
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

object OrderAdapter {
    fun get(
        onClickCallback: ((OrderModel) -> Unit)? = null,
        onPhoneButtonClickCallback: ((String) -> Unit)? = null,
        onCopyButtonClickCallback: ((String) -> Unit)? = null
    ) =
        adapterOf<OrderModel> {
            diff(
                areContentsTheSame = { old, new ->
                    old.title == new.title
                            && old.address == new.address
                            && old.customer == new.customer
                            && old.date == new.date
                            && old.description == new.description
                            && old.phoneNumber == new.phoneNumber
                            && old.shopList == new.shopList
                },
                areItemsTheSame = { old, new -> old == new }
            )
            register(
                layoutResource = R.layout.item_order,
                viewHolder = ::OrderViewHolder,
                onBindViewHolder = { vh, pos, item ->
                    vh.itemView.setOnClickListener {
                        onClickCallback?.invoke(item)
                    }

                    with(vh.binding) {
                        tvTitle.text = "${item.title} $pos"
                        tvPositions.text = item.getRefactoredShopList()
                        tvClient.text = item.customer
                        tvAddress.text = item.address
                        tvDescription.text = item.description
                        tvDate.text = dateToStrForDisplay(item.date)
                        buttonCall.text = item.phoneNumber
                        buttonCall.setOnClickListener { onPhoneButtonClickCallback?.invoke(item.phoneNumber) }
                        buttonCopy.setOnClickListener { onCopyButtonClickCallback?.invoke(getOrderAsCopiedText(item)) }
                    }
                }
            )
        }

    fun getOrderAsCopiedText(order: OrderModel): String {
        val builder = StringBuilder()
        with(builder) {
            append(order.customer).append("\n")
            append(dateToStrForDisplay(order.date)).append("\n")
            append(order.address).append("\n")
            append(order.description).append("\n")
            append(order.phoneNumber).append("\n")
            append(order.getRefactoredShopList())
        }
        return builder.toString()
    }
}

class OrderViewHolder(view: View) : RecyclerViewHolder<OrderModel>(view) {
    val binding = ItemOrderBinding.bind(view)
}
