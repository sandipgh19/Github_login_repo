package sandip.example.com.github_login_repo.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sandip.example.com.github_login_repo.data.GithubDao
import sandip.example.com.github_login_repo.database.GithubDatabase
import sandip.example.com.github_login_repo.remote.WebServices
import sandip.example.com.github_login_repo.repo.AppRepository
import sandip.example.com.github_login_repo.utils.authUtils.WebServiceHolder
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import sandip.example.com.github_login_repo.utils.remoteUtils.LiveDataCallAdapterFactory
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    // database injection
    @Provides
    @Singleton
    fun provideDatabaseModule(application: Application): GithubDatabase {
        return Room.databaseBuilder(application,
            GithubDatabase::class.java, "gitRepoRoomDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun webServiceHolderModule(): WebServiceHolder {
        return WebServiceHolder.instance
    }

    private var BASE_URL: String = ""

    @Provides
    fun provideGsonModule(): Gson = GsonBuilder().create()

    @Provides
    fun provideExecutorModule(): AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideGithubDao(database: GithubDatabase) = database.githubDao()


    @Provides
    @Singleton
    fun provideAppRepository(webservice: WebServices, executor: AppExecutors, dao: GithubDao) =  AppRepository(webservice, executor, dao)

    @Provides
    fun provideRetrofitModule(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiWebserviceModule(restAdapter: Retrofit): WebServices {
        val webService = restAdapter.create(WebServices::class.java)
        WebServiceHolder.instance.setAPIService(webService)
        return webService
    }
}