package id.mjs.etalaseapp.model

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("idTeam")
    var teamId: String? = null,

    @SerializedName("strTeam")
    var teamName: String? = null,

    @SerializedName("strSport")
    var teamSport: String? = null,

    @SerializedName("strTeamBadge")
    var teamBadge: String? = null,

    @SerializedName("strDescriptionEN")
    var teamDesc: String? = null,

    @SerializedName("intFormedYear")
    var teamFormedYear: String? = null,

    @SerializedName("strStadium")
    var teamStadium: String? = null,

    @SerializedName("strStadiumThumb")
    var teamStadiumImage: String? = null
)