package sandip.example.com.github_login_repo.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import sandip.example.com.github_login_repo.objects.Repository
import sandip.example.com.github_login_repo.repo.AppRepository
import sandip.example.com.github_login_repo.utils.remoteUtils.AbsentedLiveData
import sandip.example.com.github_login_repo.utils.remoteUtils.Resource
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(
    var repo: AppRepository
) : ViewModel(), Observable {

    private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback
    ) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback
    ) {
        callbacks.remove(callback)
    }

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

    var result: LiveData<Resource<List<Repository>>>
    var owner = MutableLiveData<String>()

    var apiCall = true

    fun init(owner: String) {
        if (apiCall)
            this.owner.value = owner
    }

    init {
        result = Transformations.switchMap(owner) {
            when (owner.value) {
                null -> AbsentedLiveData.create()
                else -> repo.loadRepos()
            }
        }
    }
}