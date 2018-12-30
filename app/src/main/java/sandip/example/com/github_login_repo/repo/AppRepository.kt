package sandip.example.com.github_login_repo.repo

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.gson.Gson
import sandip.example.com.github_login_repo.data.GithubDao
import sandip.example.com.github_login_repo.objects.LoginResponse
import sandip.example.com.github_login_repo.objects.RepoWatching
import sandip.example.com.github_login_repo.objects.Repository
import sandip.example.com.github_login_repo.remote.WebServices
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import sandip.example.com.github_login_repo.utils.remoteUtils.NetworkBoundResource
import sandip.example.com.github_login_repo.utils.remoteUtils.Resource
import javax.inject.Inject


class AppRepository @Inject constructor(
    private val webservice: WebServices,
    private val executor: AppExecutors,
    private val dao: GithubDao) {


    fun authentication(username: String): LiveData<Resource<LoginResponse>> {

        return object : NetworkBoundResource<LoginResponse, LoginResponse>(executor) {
            override fun createCall() = webservice.getUser()

            override fun shouldFetch(data: LoginResponse?) = true

            override fun saveCallResult(item: LoginResponse) {
                Log.e("Response", "Value: ${Gson().toJson(item)}")
                dao.insertDeleteUser(item)
            }

            override fun loadFromDb() = dao.fetchLogin()

        }.asLiveData()

    }


    fun loadRepos(): LiveData<Resource<List<Repository>>> {
        return object : NetworkBoundResource<List<Repository>, List<Repository>>(executor) {
            override fun saveCallResult(item: List<Repository>) {
                dao.insertDeleteRepo(item)
            }

            override fun shouldFetch(data: List<Repository>?) = true

            override fun loadFromDb() = dao.loadRepositories()

            override fun createCall() = webservice.getRepos()


        }.asLiveData()
    }

    fun loadRepoWatching(owner: String, repo: String): LiveData<Resource<List<RepoWatching>>> {
        return object : NetworkBoundResource<List<RepoWatching>, List<RepoWatching>>(executor) {
            override fun saveCallResult(item: List<RepoWatching>) {
                dao.insertDeleteRepoWatcher(watcher = item)
            }

            override fun shouldFetch(data: List<RepoWatching>?) = true

            override fun loadFromDb() = dao.loadRepositoriesWatcher()

            override fun createCall() = webservice.getRepoWatching(owner = owner, name = repo)


        }.asLiveData()
    }


}