package sandip.example.com.github_login_repo.repo

import retrofit2.Call
import sandip.example.com.github_login_repo.objects.Repository
import sandip.example.com.github_login_repo.remote.WebServices
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import sandip.example.com.github_login_repo.utils.paging.PagingBoundaryCallback

class AppListBoundaryCallaback(
    private val handleResponse: (List<Repository>?) -> Unit,
    appExecutors: AppExecutors,
    private val webService: WebServices
) : PagingBoundaryCallback<Repository, List<Repository>>(
    appExecutors = appExecutors
) {


    override fun handleAPIResponse(response: List<Repository>?) {
        handleResponse(response)
    }

    override fun itemAtEndLoaded(item: Repository): Call<List<Repository>> {
        return webService.getRepos(
            limit = item.indexInResponse
        )
    }

    override fun zeroItemLoaded(): Call<List<Repository>> {
        return webService.getRepos(
            limit = 0
        )
    }

}