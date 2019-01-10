package sandip.example.com.github_login_repo.utils

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import sandip.example.com.github_login_repo.DemoApp

class AppRunnerTest : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, DemoApp::class.java.name, context)
    }
}