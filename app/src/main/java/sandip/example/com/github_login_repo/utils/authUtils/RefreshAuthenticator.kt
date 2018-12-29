package sandip.example.com.github_login_repo.utils.authUtils

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import sandip.example.com.github_login_repo.AppController
import sandip.example.com.github_login_repo.database.PreferencesHelper
import java.io.IOException
import javax.inject.Inject

class RefreshAuthenticator @Inject constructor(
    val webservice: WebServiceHolder) : Authenticator {
    /**
     * Returns a request that includes a credential to satisfy an authentication challenge in `response`. Returns null if the challenge cannot be satisfied.
     */
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response?): Request? {

            return response?.request()?.newBuilder()
                ?.header("Authorization", PreferencesHelper(AppController.instance).authToken)
                ?.build()
        }
    }