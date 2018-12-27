package sandip.example.com.github_login_repo.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import sandip.example.com.github_login_repo.data.GithubDao
import sandip.example.com.github_login_repo.objects.LoginData

@Database(entities = [(LoginData::class)], version = 1, exportSchema = false)
abstract class GithubDatabase : RoomDatabase() {

    abstract fun githubDao(): GithubDao
}