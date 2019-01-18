package sandip.example.com.github_login_repo.repo

import retrofit2.Call
import sandip.example.com.github_login_repo.objects.Repository
import sandip.example.com.github_login_repo.remote.WebServices
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import sandip.example.com.github_login_repo.utils.paging.PagingBoundaryCallback

class AppListBoundaryCallaback(private val handleResponse: (Repository?) -> Unit,
                                        appExecutors: AppExecutors,
                                        private val webService: WebServices,
                                        private val networkPageSize: Int)
    :PagingBoundaryCallback<Repository,Repository>(
    appExecutors = appExecutors){


    override fun handleAPIResponse(response: Repository?) {
        handleResponse(response)
    }

    override fun itemAtEndLoaded(item: Repository): Call<Repository> {
        return webService.getRepos(
            limit = networkPageSize.toString())
    }

    override fun zeroItemLoaded(): Call<Repository> {
        return webService.getRepos(
            limit = networkPageSize.toString())
    }

}