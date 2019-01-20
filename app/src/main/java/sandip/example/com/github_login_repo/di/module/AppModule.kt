package sandip.example.com.github_login_repo.di.module

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.room.Room
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sandip.example.com.github_login_repo.AppController
import sandip.example.com.github_login_repo.data.GithubDao
import sandip.example.com.github_login_repo.database.GithubDatabase
import sandip.example.com.github_login_repo.database.PreferencesHelper
import sandip.example.com.github_login_repo.remote.WebServices
import sandip.example.com.github_login_repo.repo.AppRepository
import sandip.example.com.github_login_repo.utils.authUtils.RefreshAuthenticator
import sandip.example.com.github_login_repo.utils.authUtils.TLSSocketFactory
import sandip.example.com.github_login_repo.utils.authUtils.WebServiceHolder
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import sandip.example.com.github_login_repo.utils.remoteUtils.LiveDataCallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    // database injection
    @Provides
    @Singleton
    fun provideDatabaseModule(application: Application): GithubDatabase {
        return Room.databaseBuilder(
            application,
            GithubDatabase::class.java, "gitRepoRoomDatabase"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun webServiceHolderModule(): WebServiceHolder {
        return WebServiceHolder.instance
    }

    @Provides
    fun provideGsonModule(): Gson = GsonBuilder().create()

    @Provides
    fun provideExecutorModule(): AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideGithubDao(database: GithubDatabase) = database.githubDao()


    @Provides
    @Singleton
    fun provideAppRepository(
        webservice: WebServices,
        executor: AppExecutors,
        dao: GithubDao,
        pagedListConfig: PagedList.Config
    ) = AppRepository(webservice, executor, dao, pagedListConfig)


    @SuppressLint("NewApi")
    @Provides
    fun provideOkHttpClient(authenticator: RefreshAuthenticator): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .authenticator(authenticator)
            .sslSocketFactory(TLSSocketFactory())
            .addInterceptor { chain ->
                // need to intercept the request on the network layer provided by OkHttp
                val original = chain.request()
                Log.e("APICALl", "Call: ${original.url()}")


                // add request headers
                val request = original.newBuilder()
                    .header("Authorization", PreferencesHelper(AppController.instance).authToken)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        okHttpClient.sslSocketFactory()
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiWebservice(restAdapter: Retrofit): WebServices {
        val webService = restAdapter.create(WebServices::class.java)
        WebServiceHolder.instance.setAPIService(webService)
        return webService
    }

    /**
     * Pagination injection
     */
    @Provides
    @Singleton
    fun providePagingConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(true)
            .build()
    }

    @Provides
    @Singleton
    fun providePageBuilder(
        source: DataSource.Factory<Int, Any>,
        config: PagedList.Config,
        executor: AppExecutors
    ): LiveData<PagedList<Any>> {
        return LivePagedListBuilder(source, config)
            .setFetchExecutor(executor.diskIO())
            .build()
    }
}