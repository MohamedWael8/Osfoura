package com.example.hawkt.test_osfoura

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var  dummy = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_switch_dummy_data.setOnClickListener{
            dummy=true
        }

        main_button_explore.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("dummy",dummy.toString())
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }


}
