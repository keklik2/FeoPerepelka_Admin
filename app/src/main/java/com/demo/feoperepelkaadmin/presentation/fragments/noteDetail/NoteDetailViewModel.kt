package com.demo.feoperepelkaadmin.presentation.fragments.noteDetail

import android.app.Application
import android.graphics.Bitmap
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.helpers.refactorDouble
import com.demo.architecture.helpers.refactorString
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.server.Server
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.github.terrakok.cicerone.Router
import com.parse.ParseObject
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.autorun
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    var note: ProductModel? by state(null)
    private var _categoriesList: MutableList<CategoryModel>? by state(null)
    var categories: MutableList<String>? by state(null)

    init {
        getCategories()

        autorun(::_categoriesList, ::note) { list, n ->
            list?.let {
                categories = it.map { it.title }.toMutableList()
            }
            n?.let {
                if (categories == null) categories = mutableListOf(it.category)
                else {
                    if (!categories!!.contains(it.category)) categories =
                        categories!!.apply { add(it.category) }.toMutableList()
                }
            }
        }
    }

    fun saveItem(
        title: String?,
        category: String?,
        enabled: Boolean,
        description: String?,
        weight: String?,
        price: String?,
        imgTitle: String?,
        img: Bitmap
    ) {
        val rTitle = refactorString(title)
        val rCategory = refactorString(category)
        val rDescription = refactorString(description)
        val rWeight = refactorDouble(weight)
        val rPrice = refactorDouble(price)
        val rImgTitle = refactorString(imgTitle)

        Server.addOrUpdateProduct(
            ProductModel(
                rTitle,
                rCategory,
                enabled,
                rDescription,
                rWeight,
                rPrice,
                rImgTitle,
                img,
                note?.parseObject ?: ParseObject(ProductModel.ENTITY_NAME)
            )
        ) {
            showAlert(
                AppDialogContainer(
                    title = getString(R.string.dialog_error),
                    message = it.toString(),
                    positiveBtnCallback = { }
                )
            )
        }
    }

    private fun getCategories() {
        Server.getAllCategories(
            { _categoriesList = it.toMutableList() },
            {
                showAlert(
                    AppDialogContainer(
                        title = getString(R.string.dialog_error),
                        message = it.toString(),
                        positiveBtnCallback = { getCategories() },
                        negativeBtnCallback = { }
                    )
                )
            }
        )
    }

}
