package sandip.example.com.github_login_repo.utils.paging

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagingRequestHelper
import sandip.example.com.github_login_repo.utils.remoteUtils.Status

fun PagingRequestHelper.createStatusLiveData(): LiveData<Status> {
    val liveData = MutableLiveData<Status>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(Status.LOADING)
            report.hasError() -> liveData.postValue(Status.ERROR)
            else -> liveData.postValue(Status.SUCCESS)
        }
    }
    return liveData
}