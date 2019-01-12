package com.example.hawkt.test_osfoura


import com.twitter.sdk.android.core.internal.TwitterApi
import retrofit2.Call
import com.twitter.sdk.android.core.models.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.twitter.sdk.android.core.models.BindingValuesAdapter
import com.twitter.sdk.android.core.models.BindingValues
import com.twitter.sdk.android.core.models.SafeMapAdapter
import com.twitter.sdk.android.core.models.SafeListAdapter
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import retrofit2.http.*
import android.util.Log
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.internal.network.OAuth1aInterceptor
import com.twitter.sdk.android.core.internal.network.OkHttpClientHelper
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException


//Interface that holds the Http Requests their details and the Companion Object for calling them
interface UserServices
{

    @FormUrlEncoded
    @POST("oauth2/token")
    fun postCredentials(@Field("grant_type") grantType: String): Call<OAuthToken>


    @GET("/1.1/users/search.json?")
    fun search(

        @Query("q") q: String,
        @Query("page") page: Int?,
        @Query("count") count: Int?,
        @Query("include_entities") includeEntities: Boolean?
    ) : Call<List<User>>


    @GET("/1.1/users/show.json?")
    fun show(
        @Query("Name") name: String?,
        @Query("user_id") user_id: Int,
        @Query("screen_name") page: Int?,
        @Query("include_entities") includeEntities: Boolean?
    ) : Call<User>

   companion object {
        fun create() : UserServices
        {

            //Custom Logging attempt A
            /*val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger
            {
                override fun log(message: String) {
                    Log.d("OkHttp" , message)
                }
            })
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            val httpClient = OkHttpClient.Builder().addInterceptor(logging).build()*/
            //Custom Logging attempt B
            /*val httpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor
            {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val original = chain.request()

                    val request = original.newBuilder()
                        .addHeader("Authorization",R.string.AUTH_STRING.toString())
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build()

                    val okHeaders = request.headers()

                    Log.d("HTTPHeaders", okHeaders.toString())
                    return chain.proceed(request)
                }
            }).build()*/

            val retrofit =  Retrofit.Builder()
                .client(OkHttpClientHelper.getOkHttpClient(TwitterCore.getInstance().guestSessionProvider))
                .baseUrl(TwitterApi().baseHostUrl)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .build()
            return retrofit.create(UserServices::class.java)
        }
        //Attempt : Overloaded function create so that we can send it the client directly
       fun create( httpClient : OkHttpClient) : UserServices
       {
           val retrofit =  Retrofit.Builder()
               .client(httpClient)
               .baseUrl(TwitterApi().baseHostUrl)
               .addConverterFactory(GsonConverterFactory.create(buildGson()))
               .build()
           return retrofit.create(UserServices::class.java)
       }

        //Gson Builder part of Companion object for Parsing
       private fun buildGson(): Gson {
           return GsonBuilder()
               .registerTypeAdapterFactory(SafeListAdapter())
               .registerTypeAdapterFactory(SafeMapAdapter())
               .registerTypeAdapter(BindingValues::class.java, BindingValuesAdapter())
               .create()
       }
    }
}