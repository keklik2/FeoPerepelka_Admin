package com.demo.feoperepelkaadmin.server.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import com.parse.ParseObject
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream

@Parcelize
data class ProductModel(
    var title: String,
    var category: String,
    var enabled: Boolean = ENABLE,
    var description: String = UNDEFINED_STRING,
    var weight: Double,
    var price: Double,
    var imgTitle: String,
    var img: Bitmap,
    var parseObject: ParseObject? = ParseObject(ENTITY_NAME)
): Parcelable {

    fun getImgAsByteArray(): ByteArray {
        return codeImgToByteArray(this.img)
    }

    companion object {
        private const val ENABLE = true
        private const val UNDEFINED_STRING = "-"

        const val ENTITY_NAME = "ProductModel"
        const val TITLE_KEY = "title"
        const val CATEGORY_KEY = "categoryItem"
        const val ENABLED_KEY = "enabled"
        const val DESCRIPTION_KEY = "description"
        const val WEIGHT_KEY = "weight"
        const val PRICE_KEY = "price"
        const val IMG_KEY = "img"
        const val IMG_TITLE_KEY = "imgTitle"
        const val PARSE_OBJ_KEY = "parseObj"

        fun decodeImgFromByteArray(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(
                byteArray,
                0,
                byteArray.size
            )
        }

        fun codeImgToByteArray(bitmap: Bitmap): ByteArray {
            return ByteArrayOutputStream().apply {
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, this)
            }.toByteArray()
        }
    }
}
