package sandip.example.com.github_login_repo.remote

import android.arch.lifecycle.LiveData
import sandip.example.com.github_login_repo.objects.LoginResponse
import sandip.example.com.github_login_repo.utils.remoteUtils.ApiResponse
import retrofit2.http.*


interface WebServices {

    @GET("user")
    fun getUser() :LiveData<ApiResponse<LoginResponse>>
}