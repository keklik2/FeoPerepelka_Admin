package com.demo.feoperepelkaadmin.presentation.fragments.categoriesList

import android.view.View
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.ItemCategoryBinding
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

object CategoryAdapter {
    fun get(onTitleChangedCallback: ((CategoryModel) -> Unit)? = null) =
        adapterOf<CategoryModel> {
            diff(
                areContentsTheSame = { old, new -> old.title == new.title && old.locked == new.locked },
                areItemsTheSame = { old, new -> old == new }
            )
            register(
                layoutResource = R.layout.item_category,
                viewHolder = ::CategoryViewHolder,
                onBindViewHolder = { vh, _, item ->
                    // TODO("Change textView on textInputLayout")
                    // TODO("Make category title changes on inputLayout change")
                    // vh.binding.til.setOnTextChangedListener { onTitleChangedCallback?.invoke(item) }
                    vh.binding.tvTitle.text = item.title
                    vh.binding.ivLockedIcon.setVisibility(item.locked)
                }
            )
        }
}

class CategoryViewHolder(view: View): RecyclerViewHolder<CategoryModel>(view) {
    val binding = ItemCategoryBinding.bind(view)
}
