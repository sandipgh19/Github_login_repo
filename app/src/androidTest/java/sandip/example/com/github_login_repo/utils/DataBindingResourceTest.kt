package sandip.example.com.github_login_repo.utils

import android.databinding.DataBindingComponent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import sandip.example.com.github_login_repo.R
import sandip.example.com.github_login_repo.SingleFragmentActivity
import java.util.concurrent.CountDownLatch
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4::class)
class DataBindingIdlingResourceTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private val idlingResource = DataBindingResourceIdling(activityRule)
    private val fragment = TestFragment()

    @Before
    fun init() {
        IdlingRegistry.getInstance().register(idlingResource)
        activityRule.activity.replaceFragment(fragment)
        Espresso.onIdle()
    }

    @After
    fun unregister() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun alreadyIdle() {
        setHasPendingBindings(false)
        assertThat(isIdle(), `is`(true))
    }

    @Test
    fun noIdle() {
        setHasPendingBindings(true)
        assertThat(isIdle(), `is`(false))
    }

    /**
     * Ensures that we properly implement the IdlingResource API and don't call onTransitionToIdle
     * unless Espresso discovered that the resource was idle.
     */
    @Test
    fun alreadyIdle_dontCallCallbacks() {
        setHasPendingBindings(false)
        val callback = registerIdleCallback()
        isIdle()
        verify(callback, never()).onTransitionToIdle()
    }

    @Test
    fun callback_becomeIdle() {
        setHasPendingBindings(true)
        val callback = registerIdleCallback()
        isIdle()
        setHasPendingBindings(false)
        isIdle()
        verify(callback).onTransitionToIdle()
    }

    /**
     * Ensures that we can detect idle w/o relying on Espresso's polling to speed up tests
     */
    @Test
    fun callback_becomeIdle_withoutIsIdle() {
        setHasPendingBindings(true)
        val idle = CountDownLatch(1)
        idlingResource.registerIdleTransitionCallback {
            idle.countDown()
        }
        assertThat(idlingResource.isIdleNow, `is`(false))
        setHasPendingBindings(false)
        assertThat(idle.await(5, TimeUnit.SECONDS), `is`(true))
    }

    private fun setHasPendingBindings(hasPendingBindings : Boolean) {
        fragment.fakeBinding.hasPendingBindings.set(hasPendingBindings)
    }

    class TestFragment : Fragment() {
        lateinit var fakeBinding: FakeBinding
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val view = View(container!!.context)
            fakeBinding = FakeBinding(view)
            return view
        }
    }

    class FakeBinding(view: View) : ViewDataBinding(mock<DataBindingComponent>(), view, 0) {
        val hasPendingBindings = AtomicBoolean(false)

        init {
            view.setTag(R.id.dataBinding, this)
        }

        override fun setVariable(variableId: Int, value: Any?) = false

        override fun executeBindings() {}

        override fun onFieldChange(localFieldId: Int, `object`: Any?, fieldId: Int) = false

        override fun invalidateAll() {}

        override fun hasPendingBindings() = hasPendingBindings.get()

    }

    private fun isIdle(): Boolean {
        val task = FutureTask<Boolean> {
            return@FutureTask idlingResource.isIdleNow
        }

        InstrumentationRegistry.getInstrumentation().runOnMainSync(task)
        return task.get()
    }

    private fun registerIdleCallback(): IdlingResource.ResourceCallback {
        val task = FutureTask<IdlingResource.ResourceCallback> {
            val callback = mock<IdlingResource.ResourceCallback>()
            idlingResource.registerIdleTransitionCallback(callback)
            return@FutureTask callback
        }
        InstrumentationRegistry.getInstrumentation().runOnMainSync(task)
        return task.get()
    }
}