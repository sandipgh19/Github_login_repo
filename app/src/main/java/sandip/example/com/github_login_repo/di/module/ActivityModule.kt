package sandip.example.com.github_login_repo.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sandip.example.com.github_login_repo.MainActivity

@Suppress("unused")
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [(FragmentModule::class)])
    abstract fun contributeStorageActivity(): MainActivity

}