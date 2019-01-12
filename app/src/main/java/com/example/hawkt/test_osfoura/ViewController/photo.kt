package com.example.hawkt.test_osfoura

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

class photo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Photo)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
    }
}
