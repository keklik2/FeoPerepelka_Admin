package com.demo.feoperepelkaadmin.server

import android.util.Log
import com.parse.ParseException
import com.parse.ParseUser

object Login {

    const val ERR_ACCOUNT_EXISTS = 1
    const val ERR_WRONG_LOGIN = 2
    const val ERR_CONNECTION_FAILED = 3
    const val ERR_EXTRA = 4

    fun login(
        login: String,
        password: String,
        onErrorCallback: ((errCode: Int) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) {
        ParseUser.logInInBackground(login, password) { user, e ->
            if (user == null) {
                ParseUser.logOut()
                if (e != null) {
                    onErrorCallback?.let {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(ERR_CONNECTION_FAILED)
                            ParseException.OBJECT_NOT_FOUND -> it(ERR_WRONG_LOGIN)
                            else -> it(ERR_EXTRA)
                        }
                    }
                } else onSuccessCallback?.invoke()
            } else onSuccessCallback?.invoke()
        }
    }

    fun register(
        login: String,
        password: String,
        onErrorCallback: ((errCode: Int) -> Unit)? = null
    ) {
        ParseUser().apply {
            username = login
            setPassword(password)
            signUpInBackground { e ->
                ParseUser.logOut()
                onErrorCallback?.let {
                    if (e != null) {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(ERR_CONNECTION_FAILED)
                            ParseException.USERNAME_TAKEN -> it(ERR_ACCOUNT_EXISTS)
                            ParseException.OBJECT_NOT_FOUND -> it(ERR_WRONG_LOGIN)
                            else -> it(ERR_EXTRA)
                        }
                    }
                }
            }
        }
    }

    fun logout(
        onErrorCallback: ((errCode: Int) -> Unit)? = null,
        onSuccessCallback: (() -> Unit)? = null
    ) =
        ParseUser.logOutInBackground { e ->
            if (e != null) {
                onErrorCallback?.let {
                    when (e.code) {
                        ParseException.CONNECTION_FAILED -> it(ERR_CONNECTION_FAILED)
                        else -> it(ERR_EXTRA)
                    }
                }
            }
            else onSuccessCallback?.invoke()
        }

    fun isLogged(): Boolean {
        return ParseUser.getCurrentUser() != null
    }
}
