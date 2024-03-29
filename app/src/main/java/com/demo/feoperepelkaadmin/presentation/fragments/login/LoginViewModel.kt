package com.demo.feoperepelkaadmin.presentation.fragments.login

import android.app.Application
import android.util.Log
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.helpers.refactorString
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.Login
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.command
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    val switchLoading = command<Boolean>()

    fun tryLogin(login: String?, password: String?) {
        val rLogin = refactorString(login)
        val rPassword = refactorString(password)

        switchLoading(true)
        Login.login(
            rLogin,
            rPassword,
            {
                switchLoading(false)
                showAlert(
                    AppDialogContainer(
                        title = getString(R.string.dialog_title_error),
                        message = when (it) {
                            1 -> getString(R.string.dialog_error_account_not_exist)
                            2 -> getString(R.string.dialog_error_wrong_login)
                            3 -> getString(R.string.dialog_error_connection)
                            else -> getString(R.string.dialog_error)
                        },
                        positiveBtnCallback = { }
                    )
                )
            },
            { goToMainActivity() }
        )
    }

    private fun goToMainActivity() = router.replaceScreen(Screens.MainActivityScreen())
}
