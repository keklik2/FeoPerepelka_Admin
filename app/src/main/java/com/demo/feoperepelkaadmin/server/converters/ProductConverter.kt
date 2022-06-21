package com.demo.feoperepelkaadmin.server.converters

import android.graphics.BitmapFactory
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.parse.ParseObject

class ProductConverter {

    fun mapObjectToModel(parseObject: ParseObject): ProductModel {
        val byteArray = parseObject.getParseFile(ProductModel.IMG_KEY)!!.data
        val bitmap = BitmapFactory.decodeByteArray(
            byteArray,
            0,
            byteArray.size
        )
        return ProductModel(
            parseObject.getString(ProductModel.TITLE_KEY) ?: "",
            parseObject.getString(ProductModel.CATEGORY_KEY) ?: "",
            parseObject.getBoolean(ProductModel.ENABLED_KEY),
            parseObject.getString(ProductModel.DESCRIPTION_KEY) ?: "",
            parseObject.getDouble(ProductModel.WEIGHT_KEY),
            parseObject.getDouble(ProductModel.PRICE_KEY),
            bitmap,
            parseObject
        )
    }

}
