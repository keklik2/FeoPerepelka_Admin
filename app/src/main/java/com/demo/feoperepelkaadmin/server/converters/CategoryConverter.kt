package com.demo.feoperepelkaadmin.server.converters

import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.parse.ParseObject

class CategoryConverter {

    fun mapObjectToModel(parseObject: ParseObject): CategoryModel =
        CategoryModel(
            parseObject.getString(CategoryModel.TITLE_KEY) ?: "",
            parseObject.getBoolean(CategoryModel.LOCKED_KEY),
            parseObject
        )

}
