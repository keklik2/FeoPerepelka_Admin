package com.demo.architecture

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.aartikov.sesame.property.PropertyHost
import java.util.*

abstract class BaseViewModel(
    private val app: Application
): AndroidViewModel(app), PropertyHost {

    fun withScope(func: suspend () -> Unit) {
        viewModelScope.launch { func() }
    }

    fun getCurrentDate(): Long = Date().time

    fun getString(resource: Int): String = app.getString(resource)
    fun getDrawable(resource: Int): Drawable? = app.getDrawable(resource)

    override val propertyHostScope: CoroutineScope
        get() = viewModelScope
}
