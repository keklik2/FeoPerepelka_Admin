package com.demo.architecture.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AppSingleChoiceListDialog(
    private val container: AppListDialogContainer
): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).apply {
            setTitle(container.title)
            setItems(container.choiceList.toTypedArray()) { _, id ->
                container.onItemSelectedCallback?.invoke(container.choiceList[id])
            }
            container.onDismissCallback?.let { setOnDismissListener { it() } }
        }.create()
    }
}
