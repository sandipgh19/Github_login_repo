package sandip.example.com.github_login_repo.utils.authUtils

import sandip.example.com.github_login_repo.remote.WebServices

class WebServiceHolder {
    private var webservice: WebServices? = null

    fun apiService(): WebServices? {
        return this.webservice
    }

    fun setAPIService(webservice: WebServices) {
        this.webservice = webservice
    }

    companion object {
        internal var webServiceHolder: WebServiceHolder? = null

        val instance: WebServiceHolder
            get() {
                if (webServiceHolder == null) {
                    webServiceHolder = WebServiceHolder()
                }
                return webServiceHolder!!
            }
    }
}