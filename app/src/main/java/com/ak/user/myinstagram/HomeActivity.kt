package com.ak.user.myinstagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : BaseActivity(0) {

     private val TAG = "HomeActivity"
    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupButtonNavigation()
        Log.d(TAG, "onCreate")

        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

//        auth.signInWithEmailAndPassword("ahdpei68@gmail.com","280367")
//            .addOnCompleteListener{
//                if(it.isSuccessful){
//                    Log.d(TAG,"singIn success")
//                } else {
//                    Log.e(TAG,"singIn: failure",it.exception)
//                }
//            }
//
//


    }
    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Log.d(TAG,"onStart")
        }

    }

}
