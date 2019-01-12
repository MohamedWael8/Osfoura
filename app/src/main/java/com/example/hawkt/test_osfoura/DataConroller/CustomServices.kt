package com.example.hawkt.test_osfoura

import com.twitter.sdk.android.core.GuestSession
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.services.ListService

// Attempt to extend the TwitterApiClient to use its functionality instead of creating our own builder and companion object
class CustomServices(session: TwitterSession) : TwitterApiClient(session)
{

    fun getUserServices(): UserServices
    {
        return getService(UserServices::class.java)
    }
}