package sandip.example.com.github_login_repo.repo

import sandip.example.com.github_login_repo.data.GithubDao
import sandip.example.com.github_login_repo.remote.WebServices
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val webservice: WebServices,
    private val executor: AppExecutors,
    private val dao: GithubDao) {
}