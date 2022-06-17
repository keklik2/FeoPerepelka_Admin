package com.demo.feoperepelkaadmin.server.models

import android.os.Parcelable
import com.parse.ParseObject
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    var title: String,
    var categoryItem: CategoryModel,
    var enabled: Boolean = ENABLE,
    var description: String = UNDEFINED_STRING,
    var weight: Double,
    var price: Double,
    var imgUrl: String = UNDEFINED_STRING,
    var parseObject: ParseObject = ParseObject(ENTITY_NAME)
): Parcelable {
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
        const val IMG_URL_KEY = "imgUrl"
    }
}
