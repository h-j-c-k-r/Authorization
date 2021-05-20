package test.handh.authorization.ui.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import test.handh.authorization.R
import test.handh.authorization.app.extensions.hideKeyboard
import test.handh.authorization.app.extensions.showError
import test.handh.authorization.app.extensions.showSnackbar
import test.handh.authorization.databinding.FragmentAuthorizationBinding
import test.handh.authorization.ui.states.WeatherState
import test.handh.authorization.viewmodels.AuthorizationViewModel

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val binding: FragmentAuthorizationBinding by viewBinding(FragmentAuthorizationBinding::bind)

    private val viewModel: AuthorizationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPasswordIcon(true)
        setupListeners()
        setupObservers()
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
                if (invalidateFields())
                    viewModel.getWeather(35, 139)
            }

            toolbar.setOnMenuItemClickListener {
                if (invalidateFields()) {
                    showSnackbar(binding.root, R.string.hint_user_created, Snackbar.LENGTH_SHORT)
                }
                true
            }
        }
    }

    private fun setupObservers() {
        viewModel.weatherState.observe(viewLifecycleOwner) {
            when (it) {
                is WeatherState.Success -> {
                    showLoading(false)
                    showSnackbar(binding.root, it.response.weather[0].description, Snackbar.LENGTH_LONG)
                }
                WeatherState.Loading -> {
                    showLoading(true)
                }
                is WeatherState.Error -> {
                    showLoading(false)
                    showSnackbar(binding.root, it.message, Snackbar.LENGTH_SHORT, R.color.hint)
                }
                WeatherState.Idle -> {
                    showLoading(false)
                }
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
                tilEmail.showError(true, getString(R.string.error_incorrect_email))
                result = false
            }
            if (!Regex(REGEX_PASSWORD).matches(etPassword.text.toString())) {
                tilPassword.showError(true, getString(R.string.error_incorrect_password))
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

    private fun showLoading(show: Boolean) {
        with(binding) {
            content.isVisible = !show
            if (show) {
                loader.show()
            }
            else {
                loader.hide()
            }
        }
    }

    companion object {
        private const val REGEX_EMAIL =
            "[A-Z0-9a-z._%+\\-]{1,256}@[A-Za-z0-9.\\-]{1,256}\\.[A-Za-z]{2,64}"
        private const val REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{6,}\$"
    }
}