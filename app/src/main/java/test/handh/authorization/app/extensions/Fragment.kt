package test.handh.authorization.app.extensions

import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    view?.let { requireActivity().hideKeyboard(it) }
}