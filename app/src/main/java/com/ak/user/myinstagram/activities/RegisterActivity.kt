package com.ak.user.myinstagram.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ak.user.myinstagram.R

class RegisterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Log.d("RegisterActivity" , "OnCreate")
        if(savedInstanceState==null)
        {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout,EmailFragment()).commit()
        }



    }
}


// 1- e-mail, next button

class  EmailFragment :Fragment () {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
      return inflater.inflate(R.layout.fragment_register_email,container,false)
    }
}

// 2-Full name? password? register button

class  NamePassFragment :Fragment () {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_register_namepass,container,false)
    }
}