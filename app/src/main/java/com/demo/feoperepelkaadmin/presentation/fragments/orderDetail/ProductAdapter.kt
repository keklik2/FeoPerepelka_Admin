package com.demo.feoperepelkaadmin.presentation.fragments.orderDetail

import android.view.View
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.ItemProductBinding
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

object ProductAdapter {
    fun get(
        onAmountChangedCallback: ((ProductItem) -> Unit)? = null,
        onItemDeleteCallback: ((ProductItem) -> Unit)? = null
    ) =
        adapterOf<ProductItem> {
            diff(
                areContentsTheSame = { old, new -> old.title == new.title && old.amount == new.amount },
                areItemsTheSame = { old, new -> old == new }
            )
            register(
                layoutResource = R.layout.item_product,
                viewHolder = ::ProductViewHolder,
                onBindViewHolder = { vh, _, item ->
                    with(vh.binding) {
                        tvTitle.text = item.title
                        tvPositionAmount.text = item.amount.toString()

                        addPositionButton.setOnClickListener {
                            item.amount = item.amount + 1
                            tvPositionAmount.text = item.amount.toString()
                            onAmountChangedCallback?.invoke(item)
                        }
                        deletePositionButton.setOnClickListener {
                            if (item.amount > 0) {
                                item.amount = item.amount - 1
                                tvPositionAmount.text = item.amount.toString()
                                onAmountChangedCallback?.invoke(item)
                            }
                        }
                        btnDelete.setOnClickListener { onItemDeleteCallback?.invoke(item) }
                    }
                }
            )
        }
}

class ProductViewHolder(view: View) : RecyclerViewHolder<ProductItem>(view) {
    val binding = ItemProductBinding.bind(view)
}
