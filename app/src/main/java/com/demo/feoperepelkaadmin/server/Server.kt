package com.demo.feoperepelkaadmin.server

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
                } else if (objects != null)
                    onSuccessCallback.invoke(objects.map { cConverter.mapObjectToModel(it) })
                else onSuccessCallback.invoke(mutableListOf())
            }
        }
    }

    fun getAllCategories(
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ): LiveData<List<CategoryModel>> {
        val toReturn = MutableLiveData<List<CategoryModel>>()
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
                } else if (objects != null)
                    toReturn.value = objects.map { cConverter.mapObjectToModel(it) }
                else toReturn.value = mutableListOf()
            }
        }
        return toReturn
    }

    fun addOrUpdateCategory(
        category: CategoryModel,
        onErrorCallback: ((e: Exception) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) {
        with(category.parseObject) {
            put(CategoryModel.TITLE_KEY, category.title)
            put(CategoryModel.LOCKED_KEY, category.locked)
            saveInBackground { e ->
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(
                                Exception(
                                    ERR_CONNECTION_FAILED
                                )
                            )
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                } else onSuccessCallback?.invoke()
            }
        }
    }

    fun deleteCategory(
        category: CategoryModel,
        onErrorCallback: ((e: Exception) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) {
        if (!category.locked) {
            category.parseObject.deleteInBackground { e ->
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                } else onSuccessCallback?.invoke()
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
                } else if (objects != null)
                    onSuccessCallback.invoke(objects.map { pConverter.mapObjectToModel(it) })
                else onSuccessCallback.invoke(mutableListOf())
            }
        }
    }

    fun getAllProducts(
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ): LiveData<List<ProductModel>> {
        val toReturn = MutableLiveData<List<ProductModel>>()
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
                } else if (objects != null)
                    toReturn.value = objects.map { pConverter.mapObjectToModel(it) }
                else toReturn.value = mutableListOf()
            }
        }
        return toReturn
    }

    fun addOrUpdateProduct(
        product: ProductModel,
        onErrorCallback: ((e: Exception) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) {
        with(product.parseObject!!) {
            put(ProductModel.TITLE_KEY, product.title)
            put(ProductModel.CATEGORY_KEY, product.category)
            put(ProductModel.ENABLED_KEY, product.enabled)
            put(ProductModel.DESCRIPTION_KEY, product.description)
            put(ProductModel.WEIGHT_KEY, product.weight)
            put(ProductModel.PRICE_KEY, product.price)
            put(ProductModel.IMG_TITLE_KEY, product.imgTitle)
            put(ProductModel.IMG_KEY, ParseFile(product.getImgAsByteArray()))
            saveInBackground { e ->
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(
                                Exception(
                                    ERR_CONNECTION_FAILED
                                )
                            )
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                } else onSuccessCallback?.invoke()
            }
        }
    }

    fun deleteProduct(
        product: ProductModel,
        onErrorCallback: ((e: Exception) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) {
        product.parseObject!!.deleteInBackground { e ->
            if (e != null) {
                onErrorCallback?.let {
                    when (e.code) {
                        ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                        else -> it(Exception(ERR_EXTRA))
                    }
                }
            } else onSuccessCallback?.invoke()
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
                } else if (objects != null)
                    onSuccessCallback.invoke(objects.map { oConverter.mapObjectToModel(it) })
                else onSuccessCallback.invoke(mutableListOf())
            }
        }
    }

    fun getAllOrders(
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ): LiveData<List<OrderModel>> {
        val toReturn = MutableLiveData<List<OrderModel>>()
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
                } else if (objects != null)
                    toReturn.value = objects.map { oConverter.mapObjectToModel(it) }
                else toReturn.value = mutableListOf()
            }
        }
        return toReturn
    }

    fun updateOrder(
        order: OrderModel,
        onErrorCallback: ((e: Exception) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) {
        with(order.parseObject) {
            put(OrderModel.TITLE_KEY, order.title)
            put(OrderModel.SHOP_LIST_KEY, order.shopList)
            put(OrderModel.CUSTOMER_KEY, order.customer)
            put(OrderModel.ADDRESS_KEY, order.address)
            put(OrderModel.DESCRIPTION_KEY, order.description)
            put(OrderModel.PHONE_KEY, order.phoneNumber)
            put(OrderModel.DATE_KEY, order.date)
            saveInBackground { e ->
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(
                                Exception(
                                    ERR_CONNECTION_FAILED
                                )
                            )
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                } else onSuccessCallback?.invoke()
            }
        }
    }


    /**
     * Function to test orders logic
     * !! Will be deleted in release version
     */
    private var count = 0
    fun addTestOrder(
        onErrorCallback: ((e: Exception) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) {
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
            put(OrderModel.PHONE_KEY, "+7999999999${count++}")
            put(OrderModel.DATE_KEY, Date().time)
            saveInBackground { e ->
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }
                else onSuccessCallback?.invoke()
            }
        }
    }

    fun deleteOrder(
        order: OrderModel,
        onErrorCallback: ((e: Exception) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) {
        order.parseObject.deleteInBackground { e ->
            if (e != null) {
                onErrorCallback?.let {
                    when (e.code) {
                        ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                        else -> it(Exception(ERR_EXTRA))
                    }
                }
            } else onSuccessCallback?.invoke()
        }
    }
}
