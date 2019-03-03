package sandip.example.com.github_login_repo.utils

import android.support.test.espresso.IdlingRegistry
import android.support.test.rule.ActivityTestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit rule that registers an idling resource for all fragment views that use data binding.
 */
class DataBindingResourceIdlingRule (
    activityTestRule: ActivityTestRule<*>
) : TestWatcher() {

    private val idlingResource = DataBindingResourceIdling(activityTestRule)

    override fun finished(description: Description?) {
        IdlingRegistry.getInstance().unregister(idlingResource)
        super.finished(description)
    }

    override fun starting(description: Description?) {
        IdlingRegistry.getInstance().register(idlingResource)
        super.starting(description)
    }
}