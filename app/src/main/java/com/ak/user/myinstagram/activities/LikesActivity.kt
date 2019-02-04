package com.ak.user.myinstagram.activities

import android.os.Bundle
import android.util.Log
import com.ak.user.myinstagram.R


class LikesActivity : BaseActivity(3) {

    private val TAG = "LikesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
       setupButtonNavigation()
        Log.d(TAG, "onCreate")

    }
}
