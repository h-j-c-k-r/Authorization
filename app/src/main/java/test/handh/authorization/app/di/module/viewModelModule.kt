package test.handh.authorization.app.di.module

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import test.handh.authorization.viewmodels.AuthorizationViewModel

val viewModelModule = module {
    viewModel { AuthorizationViewModel(get()) }
}