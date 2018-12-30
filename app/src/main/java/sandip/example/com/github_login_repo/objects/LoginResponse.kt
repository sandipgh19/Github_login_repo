package sandip.example.com.github_login_repo.objects

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "login_table",
	primaryKeys = ["login"])
class LoginResponse {

	@field:SerializedName("login")
	lateinit var login: String

	@field:SerializedName("gists_url")
	var gistsUrl: String? = null

	@field:SerializedName("repos_url")
	var reposUrl: String? = null

	@field:SerializedName("following_url")
	var followingUrl: String? = null

	@field:SerializedName("bio")
	var bio: String? = null

	@field:SerializedName("created_at")
	var createdAt: String? = null

	@field:SerializedName("type")
	var type: String? = null

	@field:SerializedName("blog")
	var blog: String? = null

	@field:SerializedName("subscriptions_url")
	var subscriptionsUrl: String? = null

	@field:SerializedName("updated_at")
	var updatedAt: String? = null

	@field:SerializedName("site_admin")
	var siteAdmin: Boolean? = null

	@field:SerializedName("company")
	var company: String? = null

	@field:SerializedName("id")
	var id: Int? = null

	@field:SerializedName("public_repos")
	var publicRepos: Int? = null

	@field:SerializedName("gravatar_id")
	var gravatarId: String? = null

	@field:SerializedName("email")
	var email: String? = null

	@field:SerializedName("organizations_url")
	var organizationsUrl: String? = null

	@field:SerializedName("hireable")
	var hireable: Boolean? = null

	@field:SerializedName("starred_url")
	var starredUrl: String? = null

	@field:SerializedName("followers_url")
	var followersUrl: String? = null

	@field:SerializedName("public_gists")
	var publicGists: Int? = null

	@field:SerializedName("url")
	var url: String? = null

	@field:SerializedName("received_events_url")
	var receivedEventsUrl: String? = null

	@field:SerializedName("followers")
	var followers: Int? = null

	@field:SerializedName("avatar_url")
	var avatarUrl: String? = null

	@field:SerializedName("events_url")
	var eventsUrl: String? = null

	@field:SerializedName("html_url")
	var htmlUrl: String? = null

	@field:SerializedName("following")
	var following: Int? = null

	@field:SerializedName("name")
	var name: String? = null

	@field:SerializedName("location")
	var location: String? = null

	@field:SerializedName("node_id")
	var nodeId: String? = null
}