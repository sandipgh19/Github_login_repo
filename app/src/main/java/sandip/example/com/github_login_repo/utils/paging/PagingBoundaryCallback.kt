package sandip.example.com.github_login_repo.utils.paging

import android.arch.paging.PagingRequestHelper
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors

abstract class PagingBoundaryCallback<T, R>(
    private val appExecutors: AppExecutors
) : PagedList.BoundaryCallback<T>() {

    val helper = PagingRequestHelper(appExecutors.diskIO())
    val networkState = helper.createStatusLiveData()

    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            zeroItemLoaded().enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(item: T) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            itemAtEndLoaded(item).enqueue(createWebserviceCallback(it))
        }
    }

    protected abstract fun zeroItemLoaded(): Call<R>

    protected abstract fun itemAtEndLoaded(item: T): Call<R>

    protected abstract fun handleAPIResponse(response: R?)

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<R> {
        return object : Callback<R> {
            override fun onFailure(
                call: Call<R>,
                t: Throwable
            ) {
                it.recordFailure(t)
            }

            override fun onResponse(
                call: Call<R>,
                response: Response<R>
            ) {
                appExecutors.diskIO().execute {
                    handleAPIResponse(response.body())
                    it.recordSuccess()
                }
            }
        }
    }

}