package sandip.example.com.github_login_repo.objects

import com.google.gson.annotations.SerializedName

data class UserFeeds(

	@field:SerializedName("security_advisories_url")
	val securityAdvisoriesUrl: String? = null,

	@field:SerializedName("_links")
	val links: Links? = null,

	@field:SerializedName("current_user_url")
	val currentUserUrl: String? = null,

	@field:SerializedName("current_user_organization_url")
	val currentUserOrganizationUrl: String? = null,

	@field:SerializedName("current_user_organization_urls")
	val currentUserOrganizationUrls: List<String?>? = null,

	@field:SerializedName("user_url")
	val userUrl: String? = null,

	@field:SerializedName("current_user_actor_url")
	val currentUserActorUrl: String? = null,

	@field:SerializedName("current_user_public_url")
	val currentUserPublicUrl: String? = null,

	@field:SerializedName("timeline_url")
	val timelineUrl: String? = null
)