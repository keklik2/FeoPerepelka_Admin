package com.demo.feoperepelkaadmin.server.converters

import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.parse.ParseObject

class ProductConverter {

    fun mapObjectToModel(parseObject: ParseObject): ProductModel {
        val byteArray: ByteArray = parseObject.getParseFile(ProductModel.IMG_KEY)!!.data

        return ProductModel(
            parseObject.getString(ProductModel.TITLE_KEY) ?: "",
            parseObject.getString(ProductModel.CATEGORY_KEY) ?: "",
            parseObject.getBoolean(ProductModel.ENABLED_KEY),
            parseObject.getString(ProductModel.DESCRIPTION_KEY) ?: "",
            parseObject.getDouble(ProductModel.WEIGHT_KEY),
            parseObject.getDouble(ProductModel.PRICE_KEY),
            parseObject.getString(ProductModel.IMG_TITLE_KEY) ?: "",
            ProductModel.decodeImgFromByteArray(byteArray),
            parseObject
        )
    }

}
