package com.demo.feoperepelkaadmin.server.models

import com.parse.ParseObject

data class CategoryModel(
    var title: String,
    var locked: Boolean,
    var parseObject: ParseObject = ParseObject(ENTITY_NAME)
) {
    companion object {
        const val ENTITY_NAME = "CategoryModel"
        const val TITLE_KEY = "title"
        const val LOCKED_KEY = "locked"
    }
}
