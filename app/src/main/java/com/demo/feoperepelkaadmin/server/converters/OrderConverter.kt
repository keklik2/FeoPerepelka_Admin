package com.demo.feoperepelkaadmin.server.converters

import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.parse.ParseObject

class OrderConverter {

    fun mapObjectToModel(parseObject: ParseObject): OrderModel =
        OrderModel(
            parseObject.getString(OrderModel.TITLE_KEY) ?: "",
            mapOf(),
            parseObject.getString(OrderModel.CUSTOMER_KEY) ?: "",
            parseObject.getString(OrderModel.ADDRESS_KEY) ?: "",
            parseObject.getString(OrderModel.DESCRIPTION_KEY) ?: "",
            parseObject.getString(OrderModel.PHONE_KEY) ?: "",
            parseObject.getLong(OrderModel.DATE_KEY),
            parseObject
        )

}
