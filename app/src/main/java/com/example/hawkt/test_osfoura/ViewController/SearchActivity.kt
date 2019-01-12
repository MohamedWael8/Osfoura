package com.example.hawkt.test_osfoura

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import com.twitter.sdk.android.core.TwitterException
import android.util.TypedValue
import android.text.TextWatcher
import android.text.Editable
import android.widget.ImageView
import android.widget.TextView
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout.LayoutParams
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*

@Suppress("DEPRECATION", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class SearchActivity : AppCompatActivity() {

    lateinit var errorImage: ImageView
    lateinit var errorText: TextView
    lateinit var errorButton: Button
    lateinit var serchedUsersView: RecyclerView
    var searchedUser=""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

var dummy =intent.extras.get("dummy")

        if(dummy.equals("false")) {
//            if () {

            val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
            Log.d("InternetConnection", activeNetwork?.state?.toString())// this always returnes connected 
                //Twitter Configuration Code
                val config = TwitterConfig.Builder(this)
                    .logger(DefaultLogger(Log.DEBUG))
                    .twitterAuthConfig(
                        TwitterAuthConfig(
                            getString(R.string.CONSUMER_KEY),
                            getString(R.string.CONSUMER_SECRET)
                        )
                    )
                    .debug(true)
                    .build()
                //Twitter initialization code
                Twitter.initialize(config)


                //Retrofit Builder Design Code
                /*val retrofit =  Retrofit.Builder()
            .baseUrl("https://api.twitter.com/1.1/")
            .addConverterFactory(GsonConverterFactory.create())
            // Twitter interceptor
            .client(
                OkHttpClient.Builder()
                .addInterceptor(OAuth1aInterceptor(TwitterCore.getInstance().getSessionManager().getActiveSession(), TwitterCore.getInstance().getAuthConfig()))
            .build())
        .build()*/

                //Attempt at creating a manual HTTPClient request with interceptors for logging and authorization
                //var credentials = Credentials.basic(getString(CONSUMER_KEY),getString(CONSUMER_SECRET))
                //var token: OAuthToken? = null
                /*val okHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor
        {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response
            {
                val originalRequest = chain.request()
                var cred : String

                if(token != null)
                {
                    cred = token!!.authorization
                }
                else
                {
                    cred = credentials
                }
                val builder = originalRequest.newBuilder().header(
                    "Authorization",cred

                )

                val newRequest = builder.build()
                return chain.proceed(newRequest)
            }
        }).build()*/
                var previous: Int
                var error = false
                var didSearch = false
                var userDoesnotExist = false

                fun removeError() {
                    linerLayout.removeView(errorImage)
                    linerLayout.removeView(errorText)
                    linerLayout.removeView(errorButton)
                    error = false
                }

                fun removeRecyclerView() {
                    linerLayout.removeView(serchedUsersView)
                    val userParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    )
                    userParams.setMargins(38, 44, 38, 88)
                    user.layoutParams = userParams
                    didSearch = false
                }

                fun searchForUsers() {
                    val userServices = UserServices
                    // 1.1/users/search function Get http request that searches for users with against a string
                    val call = userServices.create().search(searchedUser, 1, 7, false)
                    call.enqueue(
                        object : Callback<List<User>>() {
                            override fun success(result: Result<List<User>>) {
                                print(result)
                                if (result.data.isEmpty()) {
                                    if (didSearch) {
                                        removeRecyclerView()

                                    }
                                    userDoesnotExist = true
                                    Log.d("UserTest", "user doesn't exist")
                                    //user does not exist error elements
                                    errorImage = ImageView(this@SearchActivity)
                                    errorImage.setImageResource(R.drawable.ic_error)
                                    errorImage.maxHeight = 190
                                    errorImage.maxWidth = 190
                                    errorImage.adjustViewBounds = true
                                    val params = LayoutParams(
                                        LayoutParams.MATCH_PARENT,
                                        LayoutParams.WRAP_CONTENT
                                    )
                                    params.setMargins(0, 40, 0, 0)
                                    errorImage.layoutParams = params
                                    linerLayout.addView(errorImage)

                                    errorText = TextView(this@SearchActivity)
                                    errorText.gravity = Gravity.CENTER
                                    errorText.setText(R.string.User_Does_Not_Exist)
                                    errorText.setTextColor(resources.getColor(R.color.colorBlack))
                                    errorText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)

                                    params.setMargins(0, 40, 0, 80)
                                    errorText.layoutParams = params
                                    linerLayout.addView(errorText)

                                } else {
                                    // list of users from the result of the search
                                    val userParams = LayoutParams(
                                        LayoutParams.MATCH_PARENT,
                                        LayoutParams.WRAP_CONTENT
                                    )
                                    userParams.setMargins(38, 44, 38, 0)
                                    user.layoutParams = userParams
                                    serchedUsersView = RecyclerView(this@SearchActivity)
                                    serchedUsersView.layoutManager = LinearLayoutManager(this@SearchActivity)
                                    serchedUsersView.adapter = serchedUserAdapter(result.data)
                                    val divider = DividerItemDecoration(
                                        serchedUsersView.context,
                                        LinearLayoutManager(this@SearchActivity).orientation
                                    )
                                    Log.d("UserTest", result.data[0].name)
                                    serchedUsersView.addItemDecoration(divider)
                                    val params = LayoutParams(
                                        LayoutParams.MATCH_PARENT,
                                        LayoutParams.WRAP_CONTENT
                                    )
                                    params.setMargins(0, 36, 0, 0)
                                    serchedUsersView.layoutParams = params
                                    linerLayout.addView(serchedUsersView)
                                    didSearch = true
                                }

                            }

                            override fun failure(exception: TwitterException) {
                                print(exception)
                                Log.d("UserTest", "Error" + exception.toString())
                                if (!error) {

                                    Log.d("UserTest", error.toString())

                                    // API Error
                                    error = true
                                    errorImage = ImageView(this@SearchActivity)
                                    errorImage.setImageResource(R.drawable.ic_error)
                                    errorImage.maxHeight = 190
                                    errorImage.maxWidth = 190
                                    errorImage.adjustViewBounds = true
//                            var params: LayoutParams = LayoutParams(
//                                LayoutParams.MATCH_PARENT,
//                                LayoutParams.WRAP_CONTENT
//                            )
//                            params.setMargins(0, 200, 0, 0)
//                            errorImage.layoutParams = params
                                    linerLayout.addView(errorImage)

                                    errorText = TextView(this@SearchActivity)
                                    errorText.gravity = Gravity.CENTER
                                    errorText.setText(R.string.API_error)
                                    errorText.setTextColor(resources.getColor(R.color.colorBlack))
                                    errorText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
                                    val params = LayoutParams(
                                        LayoutParams.MATCH_PARENT,
                                        LayoutParams.WRAP_CONTENT
                                    )
                                    params.setMargins(0, 35, 0, 80)
                                    errorText.layoutParams = params
                                    linerLayout.addView(errorText)

                                    errorButton = Button(this@SearchActivity)
                                    errorButton.setText(R.string.Try_Again)
                                    errorButton.setTextColor(resources.getColor(R.color.colorPrimary))
                                    errorButton.setBackgroundColor(resources.getColor(R.color.colorTransparent))
                                    errorButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
                                    errorButton.setOnClickListener {
                                        searchForUsers()
                                        removeError()

                                    }
                                    linerLayout.addView(errorButton)

                                }
                            }
                        }
                    )
                }


                user.addTextChangedListener(object : TextWatcher {


                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        previous = searchedUser.length
                        if (userDoesnotExist) {
                            userDoesnotExist = false
                            linerLayout.removeView(errorImage)
                            linerLayout.removeView(errorText)
                        }
                        if (didSearch) {
                            removeRecyclerView()
                        }


                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                Log.d("UserTest","Heyyy")

                        searchedUser = p0.toString()

                        if ((searchedUser.length == 0)) {
                            if (didSearch) {
                                removeRecyclerView()
                            }
                            if (error) {
                                removeError()
                            }
                        } else if (searchedUser.length != 0) {
                            if ((error)) {
                                removeError()
                            } else if (didSearch) {
                                Log.d("UserTest", error.toString())
                                Log.d("UserTest", didSearch.toString())
                                removeRecyclerView()

                            }


                            searchForUsers()
                        }

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })


                // UserServices is an Interface with a companion object that calls the search, show, and postCredentials


                //Attempt at 1.1/users/show get request that returns a specific user (Returns a 401)
                /*
        val call2 = userServices.create().show(null,395571736,1,false)
        call2.enqueue(
            object : Callback<User>() {
                override fun success(result: Result<User>) {
                    print(result)
                    Log.d("UserTest2", result.data.name)
                }
                override fun failure(exception: TwitterException) {
                    print(exception)
                    Log.d("UserTest2", "E" + exception.toString())
                }
            }
        )*/


                //Return userTimeLine in variable result as a List of results to access it type result.data.items[iterator] if you want
                // the next user's timeline we call the function next which returns a result(TimelineResult<Tweet>) as well timeline
                /*
        val userTimeline = UserTimeline.Builder().screenName("mwael8").maxItemsPerRequest(2).build()
        userTimeline.next(null , object : Callback<TimelineResult<Tweet>>()
        {
            override fun success(result: Result<TimelineResult<Tweet>>)
            {
                Log.d("TimeLineTest" , result.data.items[0].text)
            }

            override fun failure(exception: TwitterException)
            {
                print(exception)
                Log.d("TimeLineTest" , "Error" + exception.toString())
            }
          })
         */

                //Returns Tweet with specific id in a tweet module (class)

                /*val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService
        val call = statusesService.show(524971209851543553L, null, null, null)
        call.enqueue(object : Callback<Tweet>()
        {
            override fun success(result: Result<Tweet>)
            {
                print(result.data.text)

                Log.d("TweetTest" , "ActualResult" + result.data.text)
            }

            override fun failure(exception: TwitterException)
            {
                print(exception)
                Log.d("TweetTest" , "Error" + exception.toString())
            }
        })*/
            }
//        }
        else
        {
            user.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {



                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                Log.d("UserTest","Heyyy")
//                    var user :User
//                    user.name=""
                    if(user.text.length==1)
                    {
//                        val userParams = LayoutParams(
//                            LayoutParams.MATCH_PARENT,
//                            LayoutParams.WRAP_CONTENT
//                        )
//                        userParams.setMargins(0, 48, 0, 0)
//                        user.layoutParams = userParams
//                        serchedUsersView = RecyclerView(this@SearchActivity)
//                        serchedUsersView.layoutManager=  LinearLayoutManager(this@SearchActivity)
//                        serchedUsersView.adapter=serchedUserAdapter(result.data)
//                        val divider= DividerItemDecoration(
//                            serchedUsersView.context,
//                            LinearLayoutManager(this@SearchActivity).orientation
//                        )
//                        Log.d("UserTest", result.data[0].name)
//                        serchedUsersView.addItemDecoration(divider)
//                        val params = LayoutParams(
//                            LayoutParams.MATCH_PARENT,
//                            LayoutParams.WRAP_CONTENT
//                        )
//                        params.setMargins(0, 44, 0, 0)
//                        serchedUsersView.layoutParams = params
//                        linerLayout.addView(serchedUsersView)

                    }
                    if(user.text.length==2)
                    {
                        errorImage = ImageView(this@SearchActivity)
                        errorImage.setImageResource(R.drawable.ic_error)
                        errorImage.maxHeight = 190
                        errorImage.maxWidth = 190
                        errorImage.adjustViewBounds = true
                        val params = LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 40, 0, 0)
                        errorImage.layoutParams = params
                        linerLayout.addView(errorImage)

                        errorText = TextView(this@SearchActivity)
                        errorText.gravity = Gravity.CENTER
                        errorText.setText(R.string.User_Does_Not_Exist)
                        errorText.setTextColor(resources.getColor(R.color.colorBlack))
                        errorText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)

                        params.setMargins(0, 40, 0, 80)
                        errorText.layoutParams = params
                        linerLayout.addView(errorText)
                    }

                    if(user.text.length==3)
                    {linerLayout.removeView(errorImage)

                        linerLayout.removeView(errorText)

                        errorImage = ImageView(this@SearchActivity)
                        errorImage.setImageResource(R.drawable.ic_error)
                        errorImage.maxHeight = 190
                        errorImage.maxWidth = 190
                        errorImage.adjustViewBounds = true
                        val params = LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 40, 0, 0)
                        errorImage.layoutParams = params
                        linerLayout.addView(errorImage)

                        errorText = TextView(this@SearchActivity)
                        errorText.gravity = Gravity.CENTER
                        errorText.setText(R.string.User_Does_Not_Exist)
                        errorText.setTextColor(resources.getColor(R.color.colorBlack))
                        errorText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)

                        params.setMargins(0, 40, 0, 80)
                        errorText.layoutParams = params
                        linerLayout.addView(errorText)

                        errorText.setText(R.string.API_error)

                        errorButton = Button(this@SearchActivity)
                        errorButton.setText(R.string.Try_Again)
                        errorButton.setTextColor(resources.getColor(R.color.colorPrimary))
                        errorButton.setBackgroundColor(resources.getColor(R.color.colorTransparent))
                        errorButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
                        linerLayout.addView(errorButton)
                    }

//                    else
//                    {
//                        linerLayout.removeView(errorImage)
//
//                        linerLayout.removeView(errorText)
//                        linerLayout.removeView(errorButton)
//                    }
                    }


                override fun afterTextChanged(p0: Editable?) {

                }
            })

        }
    }
}
