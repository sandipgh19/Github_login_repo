package sandip.example.com.github_login_repo.repo

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.gson.Gson
import sandip.example.com.github_login_repo.data.GithubDao
import sandip.example.com.github_login_repo.objects.LoginResponse
import sandip.example.com.github_login_repo.remote.WebServices
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import sandip.example.com.github_login_repo.utils.remoteUtils.NetworkBoundResource
import sandip.example.com.github_login_repo.utils.remoteUtils.Resource
import javax.inject.Inject


class AppRepository @Inject constructor(
    private val webservice: WebServices,
    private val executor: AppExecutors,
    private val dao: GithubDao) {


    fun authentication(username: String, password: String): LiveData<Resource<LoginResponse>> {


        return object : NetworkBoundResource<LoginResponse, LoginResponse>(executor) {
            override fun createCall() = webservice.getUser()

            override fun shouldFetch(data: LoginResponse?) = true

            override fun saveCallResult(item: LoginResponse) {
                Log.e("Response", "Value: ${Gson().toJson(item)}")
                dao.insertLogin(item)
            }

            override fun loadFromDb() = dao.fetchLogin(login = username)

        }.asLiveData()

    }


}