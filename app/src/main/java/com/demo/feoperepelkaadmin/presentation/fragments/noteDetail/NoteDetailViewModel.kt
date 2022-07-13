package com.demo.feoperepelkaadmin.presentation.fragments.noteDetail

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import me.aartikov.sesame.property.command
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

    var imgUri: Uri? by state(null)
    var imgBtm: Bitmap? by state(null)
    var imgTitle: String? by state(null)

    val decodeBitmap = command<Uri>()

    init {
        getCategories()

        autorun(::imgUri) { uri ->
            if (uri != null) {
                decodeBitmap(uri)
                imgTitle = uri.lastPathSegment
            }
        }

        autorun(::note) { nt ->
            nt?.let {
                imgBtm = it.img
                imgTitle = it.imgTitle
            }
        }

        autorun(::_categoriesList, ::note) { list, n ->
            list?.let { nList ->
                categories = nList.map { it.title }.toMutableList()
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
        price: String?
    ) {
        val rTitle = refactorString(title)
        val rCategory = refactorString(category)
        val rDescription = refactorString(description)
        val rWeight = refactorDouble(weight)
        val rPrice = refactorDouble(price)

        if (imgBtm != null && imgTitle != null) {
            Server.addOrUpdateProduct(
                ProductModel(
                    rTitle,
                    rCategory,
                    enabled,
                    rDescription,
                    rWeight,
                    rPrice,
                    imgTitle!!,
                    imgBtm!!,
                    note?.parseObject ?: ParseObject(ProductModel.ENTITY_NAME)
                ),
                onErrorCallback = {
                    showAlert(
                        AppDialogContainer(
                            title = getString(R.string.dialog_title_error),
                            message = it.toString(),
                            positiveBtnCallback = { }
                        )
                    )
                },
                onSuccessCallback = { setCanCloseScreen() }
            )
        } else showAlert(
            AppDialogContainer(
                title = getString(R.string.dialog_title_error),
                message = getString(R.string.dialog_empty_img),
                positiveBtnCallback = { }
            )
        )
    }

    private fun getCategories() {
        Server.getAllCategories(
            { _categoriesList = it.toMutableList() },
            {
                showAlert(
                    AppDialogContainer(
                        title = getString(R.string.dialog_title_error),
                        message = it.toString(),
                        positiveBtnCallback = { getCategories() },
                        negativeBtnCallback = { }
                    )
                )
            }
        )
    }


    /**
     * Fragment exit observer
     */
    var canCloseScreen by state(false)
    private fun setCanCloseScreen() {
        canCloseScreen = true
    }
}
