package test.handh.authorization.app.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(show: Boolean, errorText: String = "") {
    isErrorEnabled = show
    error = if (show) {
        errorText
    } else {
        null
    }
}