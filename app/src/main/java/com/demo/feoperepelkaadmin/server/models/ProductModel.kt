package com.demo.feoperepelkaadmin.server.models

import android.graphics.Bitmap
import com.parse.ParseObject

data class ProductModel(
    var title: String,
    var category: String,
    var enabled: Boolean = ENABLE,
    var description: String = UNDEFINED_STRING,
    var weight: Double,
    var price: Double,
    var img: Bitmap,
    var parseObject: ParseObject? = ParseObject(ENTITY_NAME)
) {
    companion object {
        private const val ENABLE = true
        private const val UNDEFINED_STRING = "-"

        const val ENTITY_NAME = "ProductModel"
        const val TITLE_KEY = "title"
        const val CATEGORY_KEY = "categoryItem"
        const val ENABLED_KEY = "enabled"
        const val DESCRIPTION_KEY = "description"
        const val WEIGHT_KEY = "weight"
        const val PRICE_KEY = "price"
        const val IMG_KEY = "img"
    }
}
