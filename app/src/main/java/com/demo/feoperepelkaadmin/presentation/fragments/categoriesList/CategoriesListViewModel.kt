package com.demo.feoperepelkaadmin.presentation.fragments.categoriesList

import android.app.Application
import com.demo.architecture.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val app: Application
): BaseViewModel(app) {
}