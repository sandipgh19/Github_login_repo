package sandip.example.com.github_login_repo.utils

import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

/**
 * A Junit rule that registers an espresso idling resource which counts all tasks that are submitted
 * via [AppExecutors].
 */
class CountingRuleAppExecutors : TestWatcher() {
    // give it a unique id to workaround an espresso bug where you cannot register/unregister
    // an idling resource w/ the same name.
    private val id = UUID.randomUUID().toString()

    private val countingRuleAppExecutors = CountingAppExecutors {
        callbacks.forEach {
            it.onTransitionToIdle()
        }
    }

    val appExecutor = countingRuleAppExecutors.executors
    private val callbacks = CopyOnWriteArrayList<IdlingResource.ResourceCallback>()

    private val idlingResourceRule: IdlingResource = object : IdlingResource {
        override fun getName() = "App Executors Idling Resource $id"

        override fun isIdleNow() = countingRuleAppExecutors.taskCount() == 0

        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
            callbacks.add(callback)
        }
    }

    override fun finished(description: Description?) {
        countingRuleAppExecutors.drainTask(10, TimeUnit.SECONDS)
        callbacks.clear()
        IdlingRegistry.getInstance().unregister(idlingResourceRule)
        super.finished(description)
    }

    override fun starting(description: Description?) {
        IdlingRegistry.getInstance().register(idlingResourceRule)
        super.starting(description)
    }
}