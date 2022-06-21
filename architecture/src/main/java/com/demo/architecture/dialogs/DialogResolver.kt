package com.demo.architecture.dialogs

import android.content.Context
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class DialogResolver(
    private val context: Context,
    private val fragmentManager: FragmentManager
) {

    companion object {
        const val ID = "ID"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val POSITIVE_ANSWER = "positiveAnswer"
        const val NEGATIVE_ANSWER = "negativeAnswer"
        const val NEUTRAL_ANSWER = "neutralAnswer"
        const val CANCELABLE = "cancelable"
        const val ARRAY = "array"
        const val HINT = "hint"
        const val LENGTH = "length"
        const val HIDE = "hide"
        const val COPYABLE = "copyable"
    }

    private var length = Toast.LENGTH_SHORT

    fun show(type: DialogTypes, map: Map<String, Any>) {
        when (type) {
            DialogTypes.alert -> showAlert(map)
            DialogTypes.sheet -> showAlert(map)
            DialogTypes.text -> showTextQuestion(map)
            DialogTypes.loading -> showLoading(map)
            DialogTypes.toast -> showToast(map)
            DialogTypes.date -> showDate(map)
        }
    }

    private fun showDate(map: Map<String, Any>) {
        val id = map[ID].toString()
        val title = getString(map[TITLE])

        val bundle = bundleOf(
            ID to id,
            TITLE to title
        )
//        DatePickerDialog.show(fragmentManager, bundle)
    }

    private fun showLoading(map: Map<String, Any>) {
        hideLoading()
//        val hide = map.tryGetValue<Boolean>(HIDE) ?: false
//        if (hide) return

        val title = getString(map[TITLE])
//        LoadingDialogFragment.show(fragmentManager, title)
    }

    private fun hideLoading() {
//        val dialog = fragmentManager.findFragmentByTag(LoadingDialogFragment.TAG)
//        (dialog as DialogFragment?)?.dismiss()
    }


    private fun showAlert(data: Map<String, Any>) {
        try {
            val id = data[ID].toString()
            val title = getString(data[TITLE])
            val description = getString(data[DESCRIPTION])
            val positiveAnswer = getString(data[POSITIVE_ANSWER])
            val negativeAnswer = getString(data[NEGATIVE_ANSWER])
            val neutralAnswer = getString(data[NEUTRAL_ANSWER])
            val array = getStringArray(data[ARRAY])
            val copyable = getBoolean(data[COPYABLE])
            val cancelable = getBoolean(data[CANCELABLE])

            val bundle = bundleOf(
                ID to id,
                TITLE to title,
                DESCRIPTION to description,
                NEGATIVE_ANSWER to negativeAnswer,
                POSITIVE_ANSWER to positiveAnswer,
                NEUTRAL_ANSWER to neutralAnswer,
                ARRAY to array,
                COPYABLE to copyable,
                CANCELABLE to cancelable,
            )

//            DialogFragment.show(fragmentManager, bundle)
        } catch (e: Exception) {
            println(e.toString())
        }
    }

    private fun showTextQuestion(data: Map<String, Any>) {
        try {
            val id = data[ID].toString()
            val title = getString(data[TITLE])
            val description = getString(data[DESCRIPTION])
            val positiveAnswer = getString(data[POSITIVE_ANSWER])
            val negativeAnswer = getString(data[NEGATIVE_ANSWER])
            val neutralAnswer = getString(data[NEUTRAL_ANSWER])
            val hint = getString(data[HINT])
            val cancelable = getBoolean(data[CANCELABLE])

            val bundle = bundleOf(
                ID to id,
                TITLE to title,
                DESCRIPTION to description,
                NEGATIVE_ANSWER to negativeAnswer,
                POSITIVE_ANSWER to positiveAnswer,
                NEUTRAL_ANSWER to neutralAnswer,
                HINT to hint,
                CANCELABLE to cancelable,
            )

//            TextDialogFragment.show(fragmentManager, bundle)
        } catch (e: Exception) {
            println(e.toString())
        }
    }

    private fun showToast(data: Any) {
        val dic = data as Map<*, *>
        val message = getString(dic[TITLE])
        val lengthLong = dic[LENGTH] as Boolean?
        length = if (lengthLong!!) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        try {
            Toast.makeText(context, message, length).show()
        } catch (e: Exception) {
            println(e.toString())
        }
    }

    private fun getString(data: Any?): String {
        return try {
            when (data) {
                is Int -> context.resources.getString(data)
                is String -> data
                else -> data?.toString() ?: ""
            }.trim()
        } catch (e: Exception) {
            String()
        }
    }

    private fun getBoolean(data: Any?) = data as? Boolean ?: false

    private fun getStringArray(data: Any?): Array<String>? {
        if (data == null) return null
        val array = mutableListOf<String>()
        if (data is List<*>)
            data.forEach {
                try {
                    val s = when (it) {
                        is Int -> context.resources.getString(it)
                        is String -> it
                        else -> it as String
                    }
                    array.add(s)

                } catch (e: Exception) {
                    array.add(String())
                }

            }
        return array.toTypedArray()
    }

}
