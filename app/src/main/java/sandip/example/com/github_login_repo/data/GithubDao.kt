package sandip.example.com.github_login_repo.data

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*
import sandip.example.com.github_login_repo.objects.LoginResponse
import sandip.example.com.github_login_repo.objects.RepoWatching
import sandip.example.com.github_login_repo.objects.Repository

@Dao
interface GithubDao {

    @Query("SELECT * FROM login_table")
    fun fetchLogin(): LiveData<LoginResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: LoginResponse?)

    @Query("DELETE from login_table")
    fun deleteUser()

    @Transaction
    fun insertDeleteUser(user: LoginResponse?) {
        deleteUser()
        insertUser(user = user)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repositories: List<Repository>)

    @Query("DELETE FROM Repository")
    fun deleteRepos()

    @Transaction
    fun insertDeleteRepo(repositories: List<Repository>) {
        deleteRepos()
        insertRepos(repositories = repositories)
    }


//    @Query("SELECT * FROM Repository ORDER BY stars DESC")
//    fun loadRepositories(): LiveData<List<Repository>>

    @Query("SELECT * FROM Repository ORDER BY indexInResponse ASC")
    fun loadRepositories(): DataSource.Factory<Int, Repository>


    @Query(" SELECT * FROM repo_watching")
    fun loadRepositoriesWatcher(): LiveData<List<RepoWatching>>

    @Query("DELETE FROM repo_watching")
    fun deleteRepoWatcher()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepoWatcher(watcher: List<RepoWatching>)


    @Transaction
    fun insertDeleteRepoWatcher(watcher: List<RepoWatching>) {
        deleteRepoWatcher()
        insertRepoWatcher(watcher = watcher)
    }

    @Query("SELECT MAX(indexInResponse) + 1 FROM Repository")
    fun getNextIndex(): Int


    @Transaction
    fun deleteAndInsertData(list: List<Repository>) {
        deleteRepos()
        val start = getNextIndex()
        val items = list.mapIndexed { index, child ->
            child.indexInResponse = start + index
            child
        }
        insertRepos(items)
    }


}