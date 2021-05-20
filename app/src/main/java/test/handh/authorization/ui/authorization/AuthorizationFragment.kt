package test.handh.authorization.ui.authorization

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import test.handh.authorization.R
import test.handh.authorization.app.extensions.hideKeyboard
import test.handh.authorization.app.extensions.round
import test.handh.authorization.app.extensions.showError
import test.handh.authorization.app.extensions.showSnackbar
import test.handh.authorization.data.model.WeatherResponse
import test.handh.authorization.databinding.FragmentAuthorizationBinding
import test.handh.authorization.ui.states.WeatherState
import test.handh.authorization.viewmodels.AuthorizationViewModel

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val binding: FragmentAuthorizationBinding by viewBinding(FragmentAuthorizationBinding::bind)

    private val viewModel: AuthorizationViewModel by viewModel()

    private val locationListener: LocationListener by lazy { LocationListener { } }

    private val locationManager: LocationManager by lazy {
        requireContext().getSystemService(
            LOCATION_SERVICE
        ) as LocationManager
    }

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    binding.btnLogin.performClick()
                }
                else -> {
                    showSnackbar(binding.root, getString(R.string.hint_allow_get_location), Snackbar.LENGTH_LONG)
                }
            }
        }

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
                if (checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    return@setOnClickListener
                }

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_UPDATE_PERIOD,
                    LOCATION_DISTANCE, locationListener
                )

                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (invalidateFields() && location != null)
                    viewModel.getWeather(location.latitude.toInt(), location.longitude.toInt())
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
                    showWeather(it.response)
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

    private fun showWeather(weather: WeatherResponse) {
        with(weather) {
            showSnackbar(
                binding.root,
                getString(
                    R.string.success_weather_request,
                    name,
                    this.weather[0].description,
                    (main.temp - KELVIN_TO_CELSIUM).round(1),
                    (main.feelsLike - KELVIN_TO_CELSIUM).round(1),
                    (wind.speed).round(1)
                ),
                Snackbar.LENGTH_LONG
            )
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
            } else {
                loader.hide()
            }
        }
    }

    companion object {
        private const val LOCATION_UPDATE_PERIOD = 60000L
        private const val LOCATION_DISTANCE = 50f

        private const val KELVIN_TO_CELSIUM = 272.1f

        private const val REGEX_EMAIL =
            "[A-Z0-9a-z._%+\\-]{1,256}@[A-Za-z0-9.\\-]{1,256}\\.[A-Za-z]{2,64}"
        private const val REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{6,}\$"
    }
}