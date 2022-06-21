package com.demo.feoperepelkaadmin.presentation.fragments.notesList

import android.view.View
import com.demo.architecture.helpers.doubleToStrForDisplay
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.ItemNoteBinding
import com.demo.feoperepelkaadmin.server.models.ProductModel
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

object NoteAdapter {
    fun get(onClickCallback: ((ProductModel) -> Unit)? = null) =
        adapterOf<ProductModel> {
            diff(
                areContentsTheSame = { old, new ->
                    old.title == new.title
                        && old.price == new.price
                        && old.category == new.category
                        && old.enabled == new.enabled
                        && old.description == new.description
                        && old.imgTitle == new.imgTitle
                        && old.img == new.img
                        && old.weight == new.weight },
                areItemsTheSame = { old, new -> old == new }
            )
            register(
                layoutResource = R.layout.item_note,
                viewHolder = ::NoteViewHolder,
                onBindViewHolder = { vh, _, item ->
                    vh.itemView.setOnClickListener {
                        onClickCallback?.invoke(item)
                    }

                    with(vh.binding) {
                        tvTitle.text = item.title
                        tvDescription.text = item.description
                        tvPrice.text = doubleToStrForDisplay(item.price)
                        tvWeight.text = doubleToStrForDisplay(item.weight)
                        ivStatusEnabled.setVisibility(item.enabled)
                        ivStatusDisabled.setVisibility(!item.enabled)
                        ivProduct.setImageBitmap(item.img)
                    }
                }
            )
        }
}

class NoteViewHolder(view: View): RecyclerViewHolder<ProductModel>(view) {
    val binding = ItemNoteBinding.bind(view)
}
