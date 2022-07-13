package com.demo.architecture.files

import android.net.Uri

interface FileSelectionEntryPoint {
    fun onFileSelected(uri: List<Uri>?)
}
