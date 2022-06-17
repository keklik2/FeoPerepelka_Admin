package com.demo.architecture.helpers

import androidx.lifecycle.LiveData
import java.io.InvalidObjectException
import java.util.function.UnaryOperator

class ObservableList<T>(private val wrapped: MutableList<T>) : MutableList<T> by wrapped,
    LiveData<List<T>>() {
    override fun add(index: Int, element: T) = actionWithNotify { wrapped.add(index, element) }
    override fun add(element: T) = actionWithNotifyBoolean { wrapped.add(element) }
    override fun addAll(elements: Collection<T>) = actionWithNotifyBoolean { wrapped.addAll(elements) }
    override fun addAll(index: Int, elements: Collection<T>) = actionWithNotifyBoolean { wrapped.addAll(index, elements) }
    override fun remove(element: T) = actionWithNotifyBoolean { wrapped.remove(element) }
    override fun removeAll(elements: Collection<T>) = actionWithNotifyBoolean { wrapped.removeAll(elements) }
    override fun removeAt(index: Int) = actionWithNotifyResult { wrapped.removeAt(index) }
    override fun retainAll(elements: Collection<T>) = actionWithNotifyBoolean { wrapped.retainAll(elements) }
    override fun clear() = actionWithNotify { wrapped.clear() }
    override fun replaceAll(operator: UnaryOperator<T>) = actionWithNotify { wrapped.replaceAll(operator) }

    override fun getValue(): List<T> {
        return this.toList()
    }

    override fun setValue(value: List<T>?) {
        throw InvalidObjectException("ObservableList not support setValue")
    }

    init {
        update()
    }

    private fun actionWithNotify(action: () -> Unit) {
        action()
        update()
    }

    private fun actionWithNotifyBoolean(action: () -> Boolean): Boolean {
        if (action()) {
            update()
            return true
        }
        return false
    }

    private fun <T> actionWithNotifyResult(action: () -> T) : T {
        val result = action()
        update()
        return result
    }

    fun update() {
        super.setValue(this.toList())
    }
}
