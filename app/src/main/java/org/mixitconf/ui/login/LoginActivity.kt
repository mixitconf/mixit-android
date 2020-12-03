package org.mixitconf.ui.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import org.mixitconf.R
import org.mixitconf.model.entity.Credentials
import org.mixitconf.model.enums.SettingValue
import org.mixitconf.model.enums.WebsiteResponse
import org.mixitconf.model.enums.WebsiteResponseStatus
import org.mixitconf.stringSharedPrefs

class LoginActivity : AppCompatActivity() {

    //private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.email)
        val token = findViewById<EditText>(R.id.token)
        val login = findViewById<Button>(R.id.login)

        email.setText(this.stringSharedPrefs(SettingValue.EMAIL_SYNC, null))

        val model = ViewModelProvider(this).get(LoginViewModel::class.java)


        model.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                login.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                token.error = getString(loginState.passwordError)
            }
        })

        model.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (loginResult.response.status == WebsiteResponseStatus.OK) {
                updateUiWithUser("${loginResult.firstname} ${loginResult.lastname}".trim())
                setResult(Activity.RESULT_OK)
            } else {
                showLoginFailed(loginResult.response)
            }

            //Complete and destroy login activity once successful
            finish()
        })

        email.afterTextChanged {
            model.loginDataChanged(login.text.toString(), token.text.toString())
        }
        token.apply {
            afterTextChanged {
                model.loginDataChanged(login.text.toString(), token.text.toString())
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (login.isEnabled && actionId == EditorInfo.IME_ACTION_DONE) {
                    model.login(Credentials(email.text.toString(), token.text.toString()))
                }
                false
            }
        }

        token.apply {
            login.setOnClickListener {

                loading.visibility = View.VISIBLE
                model.login(Credentials(email.text.toString(), token.text.toString()))
            }
        }
    }

    fun values(): Pair<String, String> {
        return email.text.toString() to token.text.toString()
    }

    private fun updateUiWithUser(displayName: String) {
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience
        Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
                      ).show()
    }

    private fun showLoginFailed(error: WebsiteResponse) {
        val errorString: Int = when (error) {
            WebsiteResponse.TOKEN_SENT -> R.string.token_sent
            WebsiteResponse.INVALID_CREDENTIALS -> R.string.invalid_credentials
            WebsiteResponse.INVALID_EMAIL -> R.string.invalid_credentials
            WebsiteResponse.EMAIL_NOT_KNOWN -> R.string.email_not_known
            WebsiteResponse.EMAIL_SENT_ERROR -> R.string.email_sent_error
            else -> R.string.unkown_error
        }
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
