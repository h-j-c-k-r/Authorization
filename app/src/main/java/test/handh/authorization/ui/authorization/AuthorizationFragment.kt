package test.handh.authorization.ui.authorization

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
import test.handh.authorization.app.extensions.showError
import test.handh.authorization.app.extensions.showSnackbar
import test.handh.authorization.databinding.FragmentAuthorizationBinding

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val binding: FragmentAuthorizationBinding by viewBinding(FragmentAuthorizationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPasswordIcon(true)
        setupListeners()
        invalidateButtons()
    }

    private fun setupListeners() {
        with(binding) {
            etEmail.doAfterTextChanged {
                tilEmail.showError(false)
                invalidateButtons()
            }

            etPassword.doAfterTextChanged { text ->
                tilPassword.showError(false)
                invalidateButtons()
                if (text.isNullOrEmpty()) {
                    setupPasswordIcon(isCustom = true)
                } else {
                    setupPasswordIcon(isCustom = false)
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

            btnLogin.setOnClickListener {
                invalidateFields()
            }

            toolbar.setOnMenuItemClickListener {
                if (invalidateFields()) {
                    showSnackbar(binding.root, R.string.hint_user_created, Snackbar.LENGTH_SHORT)
                }
                true
            }
        }
    }

    private fun setupPasswordIcon(isCustom: Boolean) {
        with(binding.tilPassword) {
            if (isCustom) {
                endIconMode = TextInputLayout.END_ICON_CUSTOM
                endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_question)
                setEndIconOnClickListener {
                    showSnackbar(
                        binding.root,
                        R.string.hint_password_complexity,
                        Snackbar.LENGTH_SHORT
                    )
                }
                setEndIconTintList(null)
            } else {
                if (endIconMode != TextInputLayout.END_ICON_PASSWORD_TOGGLE) {
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

    private fun invalidateFields(): Boolean {
        var result = true
        with(binding) {
            if (!Regex(REGEX_EMAIL).matches(etEmail.text.toString())) {
                tilEmail.showError(true)
                result = false
            }
            if (!Regex(REGEX_PASSWORD).matches(etPassword.text.toString())) {
                tilPassword.showError(true)
                result = false
            }
        }
        return result
    }

    private fun invalidateButtons() {
        with(binding) {
            val notEmpty = !etEmail.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty()
            showMenu(notEmpty)
            btnLogin.isEnabled = notEmpty
        }
    }

    private fun showMenu(show: Boolean) {
        with(binding.toolbar) {
            if (show) {
                if (menu.size() == 0) {
                    inflateMenu(R.menu.password_menu)
                }
            } else {
                menu.clear()
            }
        }
    }

    companion object {
        private const val REGEX_EMAIL =
            "[A-Z0-9a-z._%+\\-]{1,256}@[A-Za-z0-9.\\-]{1,256}\\.[A-Za-z]{2,64}"
        private const val REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{6,}\$"
    }
}