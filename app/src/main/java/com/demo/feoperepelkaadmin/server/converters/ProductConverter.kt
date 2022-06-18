package com.demo.feoperepelkaadmin.server.converters

import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.parse.ParseObject

class ProductConverter {

    fun mapObjectToModel(parseObject: ParseObject): ProductModel =
        ProductModel(
            parseObject.getString(ProductModel.TITLE_KEY) ?: "",
            parseObject.getString(ProductModel.CATEGORY_KEY) ?: "",
            parseObject.getBoolean(ProductModel.ENABLED_KEY),
            parseObject.getString(ProductModel.DESCRIPTION_KEY) ?: "",
            parseObject.getDouble(ProductModel.WEIGHT_KEY),
            parseObject.getDouble(ProductModel.PRICE_KEY),
            parseObject.getString(ProductModel.IMG_URL_KEY) ?: "",
            parseObject
        )

}
