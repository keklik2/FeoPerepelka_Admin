package com.demo.architecture.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class SelectFileResultContract : ActivityResultContract<Unit?, List<Uri>?>() {

    override fun createIntent(context: Context, data: Unit?): Intent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri>? = when (resultCode) {
        Activity.RESULT_OK -> mutableListOf<Uri>().apply {
            intent?.clipData?.let {
                for(i in 0 until it.itemCount) {
                    add(it.getItemAt(i).uri)
                }
            }
        }
        else -> null
    }
}
