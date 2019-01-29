package com.ak.user.myinstagram

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_home.*


class ProfileActivity : BaseActivity(4){

    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupButtonNavigation()
        Log.d(TAG,"onCreate")

    }
}