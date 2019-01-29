package com.ak.user.myinstagram


import android.os.Bundle
import android.util.Log



class ShareActivity : BaseActivity(2) {

    private val TAG = "ShareActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupButtonNavigation()
        Log.d(TAG,"onCteate")

    }
}
