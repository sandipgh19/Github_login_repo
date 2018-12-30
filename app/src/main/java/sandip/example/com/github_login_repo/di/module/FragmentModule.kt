package sandip.example.com.github_login_repo.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sandip.example.com.github_login_repo.fragment.LoginFragment
import sandip.example.com.github_login_repo.fragment.RepoWatcherFragment
import sandip.example.com.github_login_repo.fragment.RepositoryListFragment

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeStorageListFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRepositoryListFragment(): RepositoryListFragment

    @ContributesAndroidInjector
    abstract fun contributeRRepoWatcherFragment(): RepoWatcherFragment

}