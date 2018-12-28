package sandip.example.com.github_login_repo.objects

import android.arch.persistence.room.Entity

@Entity(tableName = "login_github",
    primaryKeys = ["masterID"])
class LoginData {

    lateinit var masterID: String
}