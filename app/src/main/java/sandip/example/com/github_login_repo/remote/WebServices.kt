package sandip.example.com.github_login_repo.remote

import android.arch.lifecycle.LiveData
import retrofit2.Call
import sandip.example.com.github_login_repo.objects.LoginResponse
import sandip.example.com.github_login_repo.utils.remoteUtils.ApiResponse
import retrofit2.http.*
import sandip.example.com.github_login_repo.objects.RepoWatching
import sandip.example.com.github_login_repo.objects.Repository


interface WebServices {

    @GET("user")
    fun getUser() :LiveData<ApiResponse<LoginResponse>>

    @GET("user/repos")
    fun getRepos(): LiveData<ApiResponse<List<Repository>>>

    @GET("user/repos")
    fun getRepos(@Query("limit") limit: String): Call<Repository>


    @GET("repos/{owner}/{name}/subscribers")
    fun getRepoWatching(@Path("owner") owner: String,
                        @Path("name") name: String): LiveData<ApiResponse<List<RepoWatching>>>

}