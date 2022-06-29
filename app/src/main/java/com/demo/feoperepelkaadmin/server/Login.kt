package com.demo.feoperepelkaadmin.server

import com.parse.ParseException
import com.parse.ParseUser

object Login {

    const val ERR_ACCOUNT_EXISTS = "Account already exists"
    const val ERR_WRONG_LOGIN = "Wrong login or password"
    const val ERR_CONNECTION_FAILED = "Connection failed"
    const val ERR_EXTRA = "Something went wrong. Try again later"

    fun login(login: String, password: String, onErrorCallback: ((e: Exception) -> Unit)? = null) {
        ParseUser.logInInBackground(login, password) { user, e ->
            if (user == null) {
                ParseUser.logOut()
                onErrorCallback?.let {
                    if (e != null) {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            ParseException.OBJECT_NOT_FOUND -> it(Exception(ERR_WRONG_LOGIN))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }
            }
        }
    }

    fun register(
        login: String,
        password: String,
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ) {
        ParseUser().apply {
            username = login
            setPassword(password)
            signUpInBackground { e ->
                ParseUser.logOut()
                onErrorCallback?.let {
                    if (e != null) {
                        when (e.code) {
                            ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                            ParseException.USERNAME_TAKEN -> it(Exception(ERR_ACCOUNT_EXISTS))
                            ParseException.OBJECT_NOT_FOUND -> it(Exception(ERR_WRONG_LOGIN))
                            else -> it(Exception(ERR_EXTRA))
                        }
                    }
                }
            }
        }
    }

    fun logout(onErrorCallback: ((e: Exception) -> Unit)? = null) =
        ParseUser.logOutInBackground { e ->
            onErrorCallback?.let {
                if (e != null) {
                    when (e.code) {
                        ParseException.CONNECTION_FAILED -> it(Exception(ERR_CONNECTION_FAILED))
                        else -> it(Exception(ERR_EXTRA))
                    }
                }
            }
        }

    fun isLogged(): Boolean {
        return ParseUser.getCurrentUser() != null
    }
}
