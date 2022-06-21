package com.demo.feoperepelkaadmin.server

import android.graphics.Bitmap
import com.demo.feoperepelkaadmin.server.converters.CategoryConverter
import com.demo.feoperepelkaadmin.server.converters.OrderConverter
import com.demo.feoperepelkaadmin.server.converters.ProductConverter
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import java.io.ByteArrayOutputStream
import java.util.*

object Server {

    private const val ERR_NO_INTERNET = "No internet connection"

    private val cConverter = CategoryConverter()
    private val pConverter = ProductConverter()
    private val oConverter = OrderConverter()

    fun getAllCategories(onSuccessCallback: (result: List<CategoryModel>) -> Unit) {
        if (hasInternetConnection()) {
            ParseQuery.getQuery<ParseObject>(CategoryModel.ENTITY_NAME).apply {
                orderByAscending(CategoryModel.TITLE_KEY)
                findInBackground { objects, e ->
                    if (e != null) throw Exception(e)
                    else if (!objects.isNullOrEmpty())
                        onSuccessCallback.invoke(objects.map { cConverter.mapObjectToModel(it) })
                }
            }
        } else throw Exception(ERR_NO_INTERNET)
    }

    fun addOrUpdateCategory(category: CategoryModel) {
        if (hasInternetConnection()) {
            with(category.parseObject) {
                put(CategoryModel.TITLE_KEY, category.title)
                put(CategoryModel.LOCKED_KEY, category.locked)
                saveInBackground { e -> if (e != null) throw Exception(e) }
            }
        } else throw Exception(ERR_NO_INTERNET)
    }

    fun deleteCategory(category: CategoryModel) {
        if (hasInternetConnection()) category.parseObject.deleteInBackground { e ->
            if (e != null) throw Exception(e)
        }
        else throw Exception(ERR_NO_INTERNET)
    }

    fun getAllProducts(onSuccessCallback: (result: List<ProductModel>) -> Unit) {
        if (hasInternetConnection()) {
            ParseQuery.getQuery<ParseObject>(ProductModel.ENTITY_NAME).apply {
                orderByAscending(ProductModel.TITLE_KEY)
                findInBackground { objects, e ->
                    if (e != null) throw Exception(e)
                    else if (!objects.isNullOrEmpty())
                        onSuccessCallback.invoke(objects.map { pConverter.mapObjectToModel(it) })
                }
            }
        } else throw Exception(ERR_NO_INTERNET)
    }

    fun addOrUpdateProduct(product: ProductModel) {
        if (hasInternetConnection()) {
            with(product.parseObject!!) {
                put(ProductModel.TITLE_KEY, product.title)
                put(ProductModel.CATEGORY_KEY, product.category)
                put(ProductModel.ENABLED_KEY, product.enabled)
                put(ProductModel.DESCRIPTION_KEY, product.description)
                put(ProductModel.WEIGHT_KEY, product.weight)
                put(ProductModel.PRICE_KEY, product.price)
                put(ProductModel.IMG_TITLE_KEY, product.imgTitle)
                put(
                    ProductModel.IMG_KEY,
                    ParseFile(
                        ByteArrayOutputStream().apply {
                            product.img.compress(Bitmap.CompressFormat.PNG, 0, this)
                        }.toByteArray()
                    )
                )
                saveInBackground { if (it != null) throw Exception(it) }
            }
        } else throw Exception(ERR_NO_INTERNET)
    }

    fun deleteProduct(product: ProductModel) {
        if (hasInternetConnection()) product.parseObject!!.deleteInBackground { e ->
            if (e != null) throw Exception(e)
        }
        else throw Exception(ERR_NO_INTERNET)
    }

    fun getAllOrders(onSuccessCallback: (result: List<OrderModel>) -> Unit) {
        if (hasInternetConnection()) {
            ParseQuery.getQuery<ParseObject>(OrderModel.ENTITY_NAME).apply {
                orderByDescending(OrderModel.DATE_KEY)
                findInBackground { objects, e ->
                    if (e != null) throw Exception(e)
                    else if (!objects.isNullOrEmpty())
                        onSuccessCallback.invoke(objects.map { oConverter.mapObjectToModel(it) })
                }
            }
        } else throw Exception(ERR_NO_INTERNET)
    }


    /**
     * Function to test orders logic
     * !! Will be deleted in release version
     */
    private var count = 0
    fun addTestOrder() {
        if (hasInternetConnection()) {
            ParseObject(OrderModel.ENTITY_NAME).apply {
                put(OrderModel.TITLE_KEY, "Order [$count]")
                put(
                    OrderModel.SHOP_LIST_KEY, mapOf(
                        Pair("Test 1", 3),
                        Pair("Test 2", 1),
                        Pair("Test 3", 12),
                        Pair("Test 4", 2)
                    )
                )
                put(OrderModel.CUSTOMER_KEY, "Customer [$count]")
                put(OrderModel.ADDRESS_KEY, "Address [$count]")
                put(OrderModel.DESCRIPTION_KEY, "Description [$count]")
                put(OrderModel.PHONE_KEY, "Phone [${count++}]")
                put(OrderModel.DATE_KEY, Date().time)
                saveInBackground { if (it != null) throw Exception(it) }
            }
        } else throw Exception(ERR_NO_INTERNET)
    }

    fun deleteOrder(order: OrderModel) {
        if (hasInternetConnection()) order.parseObject.deleteInBackground { e ->
            if (e != null) throw Exception(e)
        }
        else throw Exception(ERR_NO_INTERNET)
    }


    fun test() {
    }


    private fun hasInternetConnection(): Boolean {
        return true
    }
}
