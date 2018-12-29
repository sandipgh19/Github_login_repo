package sandip.example.com.github_login_repo.objects

import com.google.gson.annotations.SerializedName

data class Links(

	@field:SerializedName("current_user_organization")
	val currentUserOrganization: CurrentUserOrganization? = null,

	@field:SerializedName("current_user_organizations")
	val currentUserOrganizations: List<CurrentUserOrganizationsItem?>? = null,

	@field:SerializedName("current_user_actor")
	val currentUserActor: CurrentUserActor? = null,

	@field:SerializedName("timeline")
	val timeline: Timeline? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("security_advisories")
	val securityAdvisories: SecurityAdvisories? = null,

	@field:SerializedName("current_user_public")
	val currentUserPublic: CurrentUserPublic? = null,

	@field:SerializedName("current_user")
	val currentUser: CurrentUser? = null
)