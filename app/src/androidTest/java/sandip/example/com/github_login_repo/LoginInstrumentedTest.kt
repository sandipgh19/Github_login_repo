package sandip.example.com.github_login_repo

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    private val username = "chike"
    private val password = "password"

    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java
    )

    @Test
    @Throws(Exception::class)
    fun clickLoginButton_opensLoginUi() {
        onView(withId(R.id.authenticate)).perform(click())
    }

//    @Test
//    fun shouldApplyCorrectTitle() {
//        toolbarWithTitle(R.string.login) check isDisplayed
//    }
//
//    @Test
//    fun shouldContainUsernameInput() {
//        R.id.username check isDisplayed
//    }
//
//    @Test
//    fun usernameInputShouldApplyCorrectHint() {
//        R.id.username hasHint R.string.s
//    }




}