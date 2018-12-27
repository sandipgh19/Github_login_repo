package sandip.example.com.github_login_repo.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sandip.example.com.github_login_repo.fragment.LoginFragment

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeStorageListFragment(): LoginFragment

}