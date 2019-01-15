package sandip.example.com.github_login_repo.utils

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.test.espresso.IdlingResource
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.FragmentActivity
import android.view.View
import java.util.*

class DataBindingResourceIdling(
    private val activityTestRule: ActivityTestRule<*>
) : IdlingResource {
    // list of registered callbacks
    private val idCallbacks = mutableListOf<IdlingResource.ResourceCallback>()
    // give it a unique id to workaround an espresso bug where you cannot register/unregister
    // an idling resource w/ the same name.
    private val id = UUID.randomUUID().toString()
    // holds whether isIdle is called and the result was false. We track this to avoid calling
    // onTransitionToIdle callbacks if Espresso never thought we were idle in the first place.
    private var wasNotIdle = false

    override fun getName() = "DataBinding $id"

    override fun isIdleNow(): Boolean {
        val idle = !getBindings().any { it.hasPendingBindings() }
        if (idle) {
            if (wasNotIdle) {
                // notify observers to avoid espresso race detector
                idCallbacks.forEach { it.onTransitionToIdle() }
            }
            wasNotIdle = false
        } else {
            wasNotIdle = true
            // check next frame
            activityTestRule.activity.findViewById<View>(android.R.id.content).postDelayed({
                isIdleNow
            }, 16)
        }

        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        idCallbacks.add(callback)
    }

    /**
     * Find all binding classes in all currently available fragments.
     */
    private fun getBindings(): List<ViewDataBinding> {
        return (activityTestRule.activity as? FragmentActivity)
            ?.supportFragmentManager
            ?.fragments
            ?.mapNotNull {
                it.view?.let { view ->
                    DataBindingUtil.getBinding<ViewDataBinding>(
                        view
                    )
                }
            } ?: emptyList()
    }
}