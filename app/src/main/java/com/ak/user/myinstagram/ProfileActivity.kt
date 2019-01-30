package com.ak.user.myinstagram

import android.os.Bundle
import android.util.Log


class ProfileActivity : BaseActivity(4){

    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupButtonNavigation()
        Log.d(TAG,"onCreate")

    }
}
