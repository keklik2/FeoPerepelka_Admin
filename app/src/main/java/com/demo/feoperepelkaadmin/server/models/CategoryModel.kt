package com.demo.feoperepelkaadmin.server.models

import android.os.Parcelable
import com.parse.ParseObject
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    var title: String,
    var locked: Boolean,
    var parseObject: ParseObject = ParseObject(ENTITY_NAME)
): Parcelable {
    companion object {
        const val ENTITY_NAME = "CategoryModel"
        const val TITLE_KEY = "title"
        const val LOCKED_KEY = "locked"
    }
}
