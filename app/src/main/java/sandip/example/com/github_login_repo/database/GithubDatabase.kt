package sandip.example.com.github_login_repo.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import sandip.example.com.github_login_repo.data.GithubDao
import sandip.example.com.github_login_repo.objects.LoginData
import sandip.example.com.github_login_repo.objects.LoginResponse
import sandip.example.com.github_login_repo.objects.Repository

@Database(
    entities = [(LoginData::class),
        (LoginResponse::class),
        (Repository::class)],
    version = 1, exportSchema = false
)
abstract class GithubDatabase : RoomDatabase() {

    abstract fun githubDao(): GithubDao
}