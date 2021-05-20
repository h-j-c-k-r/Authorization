package test.handh.authorization.app.extensions

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import test.handh.authorization.R

fun Fragment.hideKeyboard() {
    view?.let { requireActivity().hideKeyboard(it) }
}

fun Fragment.showSnackbar(view: View, stringResId: Int, length: Int) {
    Snackbar.make(view, stringResId, length)
        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple))
        .show()
}