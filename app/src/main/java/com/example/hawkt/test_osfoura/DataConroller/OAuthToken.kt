package com.example.hawkt.test_osfoura

import com.google.gson.annotations.SerializedName

// Class supporting the manual client authentication attempt
class OAuthToken
{

    @SerializedName("access_token")
    val accessToken: String? = null

    @SerializedName("token_type")
    val tokenType: String? = null

    val authorization: String
        get() = "$tokenType $accessToken"
}