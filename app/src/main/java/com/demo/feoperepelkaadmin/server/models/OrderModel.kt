package com.demo.feoperepelkaadmin.server.models

import com.parse.ParseObject

data class OrderModel(
    var title: String,
    var shopList: Map<ProductModel, Int> = mapOf(),
    var customer: String,
    var address: String,
    var description: String = UNDEFINED_STRING,
    var phoneNumber: String,
    var date: Long,
    var parseObject: ParseObject = ParseObject(ENTITY_NAME)
) {
    companion object {
        private const val UNDEFINED_STRING = "-"

        const val ENTITY_NAME = "OrderModel"
        const val TITLE_KEY = "title"
        const val SHOP_LIST_KEY = "shopList"
        const val CUSTOMER_KEY = "customer"
        const val ADDRESS_KEY = "address"
        const val DESCRIPTION_KEY = "description"
        const val PHONE_KEY = "phoneNumber"
        const val DATE_KEY = "date"
    }
}
