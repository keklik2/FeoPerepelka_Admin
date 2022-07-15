package com.demo.architecture.dialogs

data class AppDialogContainer(
    val title: String? = null,
    val message: String,
    val choiceList: List<String>? = null,
    val positiveBtnCallback: (() -> Unit),
    val negativeBtnCallback: (() -> Unit)? = null,
    val neutralBtnCallback: (() -> Unit)? = null,
    val dismissCallback: (() -> Unit)? = null
)
