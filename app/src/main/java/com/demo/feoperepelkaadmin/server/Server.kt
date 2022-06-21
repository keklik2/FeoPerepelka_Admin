package com.demo.feoperepelkaadmin.server

import android.graphics.Bitmap
import com.demo.feoperepelkaadmin.server.converters.CategoryConverter
import com.demo.feoperepelkaadmin.server.converters.OrderConverter
import com.demo.feoperepelkaadmin.server.converters.ProductConverter
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import java.io.ByteArrayOutputStream
import java.util.*

object Server {

    const val ERR_CONNECTION_FAILED = "Connection failed"
    const val ERR_CATEGORY_LOCKED = "Unable to delete locked category"
    const val ERR_EXTRA = "Something went wrong. Try again later"

    private val cConverter = CategoryConverter()
    private val pConverter = ProductConverter()
    private val oConverter = OrderConverter()

    fun getAllCategories(
        onSuccessCallback: (result: List<CategoryModel>) -> Unit,
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ) {
        ParseQuery.getQuery<ParseObject>(CategoryModel.ENTITY_NAME).apply {
            orderByAscending(CategoryModel.TITLE_KEY)
            findInBackground { objects, e ->
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                } else if (!objects.isNullOrEmpty())
                    onSuccessCallback.invoke(objects.map { cConverter.mapObjectToModel(it) })
            }
        }
    }

    fun addOrUpdateCategory(
        category: CategoryModel,
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ) {
        with(category.parseObject) {
            put(CategoryModel.TITLE_KEY, category.title)
            put(CategoryModel.LOCKED_KEY, category.locked)
            saveInBackground { e ->
                onErrorCallback?.let {
                    if (e != null) {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }
            }
        }
    }

    fun deleteCategory(category: CategoryModel, onErrorCallback: ((e: Exception) -> Unit)? = null) {
        if (!category.locked) {
            category.parseObject.deleteInBackground { e ->
                onErrorCallback?.let {
                    if (e != null) {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }
            }
        } else onErrorCallback?.invoke(Exception(ERR_CATEGORY_LOCKED))
    }

    fun getAllProducts(
        onSuccessCallback: (result: List<ProductModel>) -> Unit,
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ) {
        ParseQuery.getQuery<ParseObject>(ProductModel.ENTITY_NAME).apply {
            orderByAscending(ProductModel.TITLE_KEY)
            findInBackground { objects, e ->
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }else if (!objects.isNullOrEmpty())
                    onSuccessCallback.invoke(objects.map { pConverter.mapObjectToModel(it) })
            }
        }
    }

    fun addOrUpdateProduct(
        product: ProductModel,
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ) {
        with(product.parseObject!!) {
            put(ProductModel.TITLE_KEY, product.title)
            put(ProductModel.CATEGORY_KEY, product.category)
            put(ProductModel.ENABLED_KEY, product.enabled)
            put(ProductModel.DESCRIPTION_KEY, product.description)
            put(ProductModel.WEIGHT_KEY, product.weight)
            put(ProductModel.PRICE_KEY, product.price)
            put(
                ProductModel.IMG_KEY,
                ParseFile(
                    ByteArrayOutputStream().apply {
                        product.img.compress(Bitmap.CompressFormat.PNG, 0, this)
                    }.toByteArray()
                )
            )
            saveInBackground { e ->
                onErrorCallback?.let {
                    if (e != null) {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }
            }
        }
    }

    fun deleteProduct(product: ProductModel, onErrorCallback: ((e: Exception) -> Unit)? = null) {
        product.parseObject!!.deleteInBackground { e ->
            onErrorCallback?.let {
                if (e != null) {
                    when (e.code) {
                        ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                        else -> it(Exception(ERR_EXTRA))
                    }
                }
            }
        }
    }

    fun getAllOrders(
        onSuccessCallback: (result: List<OrderModel>) -> Unit,
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ) {
        ParseQuery.getQuery<ParseObject>(OrderModel.ENTITY_NAME).apply {
            orderByDescending(OrderModel.DATE_KEY)
            findInBackground { objects, e ->
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }else if (!objects.isNullOrEmpty())
                    onSuccessCallback.invoke(objects.map { oConverter.mapObjectToModel(it) })
            }
        }
    }


    /**
     * Function to test orders logic
     * !! Will be deleted in release version
     */
    private var count = 0
    fun addTestOrder(onErrorCallback: ((e: Exception) -> Unit)? = null) {
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
            saveInBackground { e ->
                onErrorCallback?.let {
                    if (e != null) {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }
            }
        }
    }

    fun deleteOrder(order: OrderModel, onErrorCallback: ((e: Exception) -> Unit)? = null) {
        order.parseObject.deleteInBackground { e ->
            onErrorCallback?.let {
                if (e != null) {
                    when (e.code) {
                        ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                        else -> it(Exception(ERR_EXTRA))
                    }
                }
            }
        }
    }
}
