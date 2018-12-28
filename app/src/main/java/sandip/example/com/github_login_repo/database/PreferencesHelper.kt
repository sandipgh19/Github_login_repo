package sandip.example.com.github_login_repo.database

import android.content.Context
import android.preference.PreferenceManager


class PreferencesHelper(var context: Context){

    companion object {
        val DEVELOP_MODE = false
        private const val AUTHENTICATION_TOKEN = "data.source.prefs.AUTHENTICATION_TOKEN"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    // save device token
    var authToken = preferences.getString(AUTHENTICATION_TOKEN, "")
        set(value) = preferences.edit().putString(AUTHENTICATION_TOKEN,value).apply()
}