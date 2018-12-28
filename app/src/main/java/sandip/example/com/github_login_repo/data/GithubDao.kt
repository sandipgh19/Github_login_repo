package sandip.example.com.github_login_repo.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import sandip.example.com.github_login_repo.objects.LoginResponse

@Dao
interface GithubDao {

    @Query("SELECT * FROM login_table WHERE login=:login")
    fun fetchLogin(login: String) : LiveData<LoginResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLogin(list: LoginResponse?)

}