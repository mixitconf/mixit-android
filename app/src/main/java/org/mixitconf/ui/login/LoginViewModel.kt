package org.mixitconf.ui.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mixitconf.R
import org.mixitconf.get
import org.mixitconf.mixitApp
import org.mixitconf.model.entity.Credentials
import org.mixitconf.model.enums.WebsiteResponse
import org.mixitconf.model.enums.WebsiteResponseStatus
import org.mixitconf.service.synchronization.dto.response
import org.mixitconf.service.synchronization.dto.responseStatus
import kotlin.coroutines.CoroutineContext

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    val loginFormState: MutableLiveData<LoginFormState> = MutableLiveData()
    val loginResult: MutableLiveData<LoginResult> = MutableLiveData()

    fun login(credentials: Credentials)  = checkCredentials(credentials)

    private fun checkCredentials(credentials: Credentials) {
        viewModelScope.launch {
        runCatching { mixitApp.websiteUserService.profile(credentials.username, credentials.token).get() }
            .onFailure {
                //loginResult.value = LoginResult(WebsiteResponse.UNKNOWN_ERROR)
                mixitApp.websiteUserService.askForToken(credentials.username).execute().run{
                    loginResult.postValue(LoginResult(WebsiteResponse.UNKNOWN_ERROR))
                }
//                runCatching {  }
//                    .onFailure {
//                        loginResult.value = LoginResult(WebsiteResponse.UNKNOWN_ERROR)
//                    }
//                    .onSuccess {
//                        loginResult.value =  LoginResult(WebsiteResponse.UNKNOWN_ERROR)
//                    }
            }
            .onSuccess {
                loginResult.value = LoginResult(WebsiteResponse.CREDENTIAL_VALID, it.login, it.firstname, it.lastname)
            }
        }
//        launch {
//            mixitApp.websiteUserService.
//
//            checkToken(credentials.username, credentials.token).execute().run {
//
//            }
//
//            runCatching {
//
//                get()
//                this.execute().run {
//                    if (isSuccessful && body() != null) {
//                        return body() as T
//                    }
//                    throw RuntimeException("Response is empty or invalid")
//                }
//
//            }
//                .onFailure {
//                    loginResult.postValue(LoginResult(WebsiteResponse.UNKNOWN_ERROR))
//                }
//                .onSuccess {
//                    if(it.responseStatus == WebsiteResponseStatus.OK){
//                        readProfile(credentials)
//                    }
//                    else {
//                        loginResult.postValue(LoginResult(it.response))
//                    }
//                }
//        }
    }

    private fun readProfile(credentials: Credentials) {
        runCatching { mixitApp.websiteUserService.profile(credentials.username, credentials.token).get() }
            .onFailure {
                loginResult.value = LoginResult(WebsiteResponse.UNKNOWN_ERROR)
            }
            .onSuccess {
                loginResult.value = LoginResult(WebsiteResponse.CREDENTIAL_VALID, it.login, it.firstname, it.lastname)
            }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            loginFormState.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            loginFormState.value = LoginFormState(passwordError = R.string.invalid_token)
        } else {
            loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
