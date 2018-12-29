package sandip.example.com.github_login_repo.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import sandip.example.com.github_login_repo.objects.LoginResponse
import sandip.example.com.github_login_repo.objects.Repository

@Dao
interface GithubDao {

    @Query("SELECT * FROM login_table WHERE login=:login or email=:login")
    fun fetchLogin(login: String) : LiveData<LoginResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLogin(list: LoginResponse?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repositories: List<Repository>)

    @Query("SELECT * FROM Repository WHERE owner_login = :ownerLogin AND name = :name")
    fun load(ownerLogin: String, name: String): LiveData<Repository>

    @Query(
        """
        SELECT * FROM Repository
        WHERE owner_login = :owner
        ORDER BY stars DESC"""
    )
    fun loadRepositories(owner: String): LiveData<List<Repository>>


}