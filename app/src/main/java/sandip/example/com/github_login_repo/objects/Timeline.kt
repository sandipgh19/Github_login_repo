package sandip.example.com.github_login_repo.objects

import com.google.gson.annotations.SerializedName

class Timeline{

	@field:SerializedName("href")
	var href: String? = null

	@field:SerializedName("type")
	var type: String? = null
}