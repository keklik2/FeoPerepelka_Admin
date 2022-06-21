package com.demo.feoperepelkaadmin.server.models

import com.parse.ParseObject

data class OrderModel(
    var title: String,
    var shopList: MutableMap<String, Int> = mutableMapOf(),
    var customer: String,
    var address: String,
    var description: String = UNDEFINED_STRING,
    var phoneNumber: String,
    var date: Long,
    var parseObject: ParseObject = ParseObject(ENTITY_NAME)
) {
    fun getRefactoredShopList(): String {
        val sb = StringBuilder()
        for (i in shopList) {
            sb.append("${i.key}(${i.value})")
            if (i.key != shopList.keys.last()) sb.append(", ")
        }
        return sb.toString()
    }

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
