package com.ak.user.myinstagram

import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : BaseActivity(0) {

     private val TAG = "HomeActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupButtonNavigation()
        Log.d(TAG, "onCreate")

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword("ahdpei68@gmail.com","280367")
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Log.d(TAG,"singIn success")
                } else {
                    Log.e(TAG,"singIn: failure",it.exception)
                }
            }




    }
}
