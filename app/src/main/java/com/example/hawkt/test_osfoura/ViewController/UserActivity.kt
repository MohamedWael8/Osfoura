package com.example.hawkt.test_osfoura

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_user.*
import android.content.Intent
import com.twitter.sdk.android.core.models.User

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val message = intent.getParcelableArrayExtra(EXTRA_MESSAGE) as User
        userName.text= message.name
//        Log.d("UserTest",message)
    }
}
