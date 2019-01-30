package com.ak.user.myinstagram

import android.os.Bundle
import android.util.Log


class LikesActivity :BaseActivity(3) {

    private val TAG = "LikesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
       setupButtonNavigation()
        Log.d(TAG, "onCreate")

    }
}
