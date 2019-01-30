package com.ak.user.myinstagram

import android.os.Bundle
import android.util.Log



class HomeActivity : BaseActivity(0) {

     private val TAG = "HomeActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupButtonNavigation()
        Log.d(TAG, "onCreate")

    }
}
