package sandip.example.com.github_login_repo.utils.paging

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import sandip.example.com.github_login_repo.utils.remoteUtils.Status

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
data class Listing<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val networkState: LiveData<Status>,
    // represents the refresh status to show to the user. Separate from networkState, this
    // value is importantly only when refresh is requested.
    val refreshState: LiveData<Status>,
    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit,
    // retries any failed requests.
    val retry: () -> Unit)