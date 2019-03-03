package sandip.example.com.github_login_repo.utils

import android.arch.core.executor.testing.CountingTaskExecutorRule
import android.os.Parcel
import android.os.Parcelable
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import org.junit.runner.Description
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

/**
 * A jUnit rule that registers Architecture Component's background threads as an Espresso idling
 * resource.
 */
class TaskExecutorResourceRule() : CountingTaskExecutorRule(), Parcelable {
    // give it a unique id to workaround an espresso bug where you cannot register/unregister
    // an idling resource w/ the same name.
    private val id = UUID.randomUUID().toString()

    private val idlingResource: IdlingResource = object : IdlingResource {
        override fun getName(): String {
            return "architecture components idling resource $id"
        }

        override fun isIdleNow(): Boolean {
            return this@TaskExecutorResourceRule.isIdle
        }

        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
            callbacks.add(callback)
        }
    }

    private val callbacks = CopyOnWriteArrayList<IdlingResource.ResourceCallback>()

    constructor(parcel: Parcel) : this() {
    }

    override fun finished(description: Description?) {
        drainTasks(10, TimeUnit.SECONDS)
        callbacks.clear()
        IdlingRegistry.getInstance().unregister(idlingResource)
        super.finished(description)
    }

    override fun onIdle() {
        callbacks.forEach {
            it.onTransitionToIdle()
        }
        super.onIdle()
    }

    override fun starting(description: Description?) {
        IdlingRegistry.getInstance().register(idlingResource)
        super.starting(description)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskExecutorResourceRule> {
        override fun createFromParcel(parcel: Parcel): TaskExecutorResourceRule {
            return TaskExecutorResourceRule(parcel)
        }

        override fun newArray(size: Int): Array<TaskExecutorResourceRule?> {
            return arrayOfNulls(size)
        }
    }
}