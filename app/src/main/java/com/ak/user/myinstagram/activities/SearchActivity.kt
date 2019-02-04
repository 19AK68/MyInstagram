package com.ak.user.myinstagram.activities

import android.os.Bundle
import android.util.Log
import com.ak.user.myinstagram.R


class SearchActivity : BaseActivity(1) {
    private val TAG = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupButtonNavigation()
        Log.d(TAG,"onCreate")

    }
}
