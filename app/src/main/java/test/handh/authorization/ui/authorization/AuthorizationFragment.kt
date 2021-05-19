package test.handh.authorization.ui.authorization

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import test.handh.authorization.R
import test.handh.authorization.app.extensions.hideKeyboard
import test.handh.authorization.databinding.FragmentAuthorizationBinding

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val binding: FragmentAuthorizationBinding by viewBinding(FragmentAuthorizationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPasswordHelper()
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            etPassword.doAfterTextChanged { text ->
                if (text.isNullOrEmpty()) {
                    setupPasswordHelper()
                } else {
                    if (tilPassword.endIconMode != TextInputLayout.END_ICON_PASSWORD_TOGGLE) {
                        tilPassword.apply {
                            endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                            setEndIconTintList(
                                ContextCompat.getColorStateList(
                                    requireContext(),
                                    R.color.hint
                                )
                            )
                        }
                    }
                }
            }
            etPassword.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
            etEmail.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun setupPasswordHelper() {
        with(binding.tilPassword) {
            endIconMode = TextInputLayout.END_ICON_CUSTOM
            endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_question)
            setEndIconOnClickListener {
                showSnackbar(R.string.hint_password_complexity, Snackbar.LENGTH_SHORT)
            }
            setEndIconTintList(null)
        }
    }

    private fun showSnackbar(stringResId: Int, length: Int) {
        Snackbar.make(
            binding.root,
            stringResId,
            length
        )
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple))
            .show()
    }
}