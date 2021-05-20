package test.handh.authorization.app.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(show: Boolean) {
    error = if (show) {
        ERROR
    } else {
        null
    }
}

private const val ERROR = "error"