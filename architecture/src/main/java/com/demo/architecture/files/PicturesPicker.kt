package com.demo.architecture.files

import android.Manifest
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.demo.architecture.R
import com.demo.architecture.dialogs.AppAlertDialog
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.helpers.isPermissionGranted

class PicturesPicker(
    private val fragment: Fragment,
    private val isMultipleChoice: Boolean = false,
    private val onSelectSuccess: ((Uri) -> Unit)? = null
): FileSelectionEntryPoint {

    private val picturesPicker =
        fragment.registerForActivityResult(SelectFileResultContract(isMultipleChoice)) {
            onFileSelected(it)
        }
    private val readStoragePermission =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            val anyNotGranted = granted.values.contains(false)
            if (!anyNotGranted) picturesPicker.launch(null)
            else {
                AppAlertDialog(
                    AppDialogContainer(
                        title = fragment.getString(R.string.dialog_permission_title),
                        message = fragment.getString(R.string.dialog_permission_message),
                        positiveBtnCallback = {}
                    )
                ).show(fragment.childFragmentManager, TAG)
            }
        }

    fun openPicturesPicker() {
            if (fragment.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                picturesPicker.launch(null)
            } else readStoragePermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
    }

    override fun onFileSelected(uri: List<Uri>?) {
        if (!uri.isNullOrEmpty()) {
            onSelectSuccess?.invoke(uri[0])
            Toast.makeText(
                fragment.requireActivity(),
                R.string.toast_file_picker_success,
                Toast.LENGTH_SHORT
            ).show()
        }
        else Toast.makeText(
            fragment.requireActivity(),
            R.string.toast_file_picker_error,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val TAG = "Pictures Picker TAG"
    }

}
