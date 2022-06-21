package com.demo.feoperepelkaadmin.presentation.fragments.orderDetail

import android.app.Application
import com.demo.architecture.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val app: Application
): BaseViewModel(app) {
}
