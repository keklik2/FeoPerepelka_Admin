package com.demo.feoperepelkaadmin.server

import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.demo.feoperepelkaadmin.server.models.ProductModel
import java.lang.Exception

object Shop {

    private lateinit var db: Any
    private val initialized = false

    private val _categories = mutableListOf<CategoryModel>()
    val categories get() = _categories
    private val _products = mutableListOf<ProductModel>()
    val products get() = _products
    private val _orders = mutableListOf<OrderModel>()
    val orders get() = _orders

    fun initialize() {
        // TODO("download orders from server")
        if (initialized) return
        // TODO("download categories & products from server")
    }

    fun addCategories(vararg category: CategoryModel) {
        for (c in category) {
            _categories.add(c)
            // TODO("add to server")
        }
    }

    @Throws
    fun deleteCategory(vararg category: CategoryModel) {
        for (c in category) {
            if (c.locked) throw Exception("Unable to delete LOCKED category")
            try {
                // TODO("remove from server")
            } catch (e: Exception) {
                throw Exception("Unable to remove category ${c.title} from server. Try again later")
            }
            _categories.remove(c)
        }
    }

    fun addProducts(vararg product: ProductModel) {
        for (p in product) {
            _products.add(p)
            // TODO("add to server")
        }
    }

    @Throws
    fun deleteProducts(vararg product: ProductModel) {
        for (p in product) {
            try {
            // TODO("remove from server")
            } catch (e: Exception) {
                throw Exception("Unable to remove product ${p.title} from server. Try again later")
            }
            _products.remove(p)
        }
    }

    @Throws
    fun closeOrders(vararg order: OrderModel) {
        for (o in order) {
            try {
                // TODO("remove from server")
            } catch (e: Exception) {
                throw Exception("Unable to remove order ${o.title} from server. Try again later")
            }
            _orders.remove(o)
        }
    }
}
