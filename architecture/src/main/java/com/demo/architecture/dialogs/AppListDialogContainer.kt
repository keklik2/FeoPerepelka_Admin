package com.demo.architecture.dialogs

data class AppListDialogContainer(
    val title: String,
    val choiceList: List<String>,
    val onItemSelectedCallback: ((title: String) -> Unit)? = null,
    val onDismissCallback: (() -> Unit)? = null
)
