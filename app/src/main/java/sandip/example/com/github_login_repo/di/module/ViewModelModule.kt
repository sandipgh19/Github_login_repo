package sandip.example.com.github_login_repo.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sandip.example.com.github_login_repo.di.ViewModelKey
import sandip.example.com.github_login_repo.factory.AppModelFactory
import sandip.example.com.github_login_repo.viewModel.LoginViewModel
import sandip.example.com.github_login_repo.viewModel.RepositoryListViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepositoryListViewModel::class)
    abstract fun bindRepositoryListViewModel(loginViewModel: RepositoryListViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: AppModelFactory): ViewModelProvider.Factory

}