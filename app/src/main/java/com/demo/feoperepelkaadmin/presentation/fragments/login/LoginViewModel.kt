package com.demo.feoperepelkaadmin.presentation.fragments.login

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.helpers.refactorString
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.presentation.Screens
import com.demo.feoperepelkaadmin.server.Login
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
): BaseViewModel(app) {

    fun tryLogin(login: String?, password: String?) {
        val rLogin = refactorString(login)
        val rPassword = refactorString(password)

        var success = true
        Login.login(
            rLogin,
            rPassword
        ) {
            success = false
            showAlert(
                AppDialogContainer(
                    title = getString(R.string.dialog_error),
                    message = it.toString(),
                    positiveBtnCallback = {  }
                )
            )
        }

        if (success) goToMainActivity()
    }

    private fun goToMainActivity() = router.replaceScreen(Screens.MainActivityScreen())
}
