package com.demo.architecture.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract

class SelectFileResultContract(
    private val isMultipleChoice: Boolean
) : ActivityResultContract<Unit?, List<Uri>?>() {

    override fun createIntent(context: Context, data: Unit?): Intent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultipleChoice)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri>? {
        return when (resultCode) {
            Activity.RESULT_OK -> mutableListOf<Uri>().apply {
                intent?.let {
                    if (it.data != null) add(it.data!!)
                    else {
                        it.clipData?.let { cd ->
                            for(i in 0 until cd.itemCount) {
                                add(cd.getItemAt(i).uri)
                            }
                        }
                    }
                }
            }
            else -> null
        }
    }
}
