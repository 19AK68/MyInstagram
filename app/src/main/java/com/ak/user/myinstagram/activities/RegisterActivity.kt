package com.ak.user.myinstagram.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ak.user.myinstagram.R
import com.ak.user.myinstagram.R.id.pass_input
import com.ak.user.myinstagram.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_namepass.*

class RegisterActivity : AppCompatActivity(), EmailFragment.Listener, NamePassFragment.Listener {

    private var mEmail:String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbase: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth =  FirebaseAuth.getInstance()
        mDbase = FirebaseDatabase.getInstance().reference

        Log.d("RegisterActivity" , "OnCreate")
        if(savedInstanceState==null)
        {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout,EmailFragment())
                .commit()
        }

    }

    override fun onNext(email: String) {
        if(email.isNotEmpty())
        {
           mEmail =email
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout,NamePassFragment())
                .addToBackStack(null).commit()
        } else{
            showTost("Please Enter email")
        }

    }

    override fun onRegister(fullName: String, password: String) {
       if(fullName.isNotEmpty() && password.isNotEmpty())
       {
           val email = mEmail

           if(email!=null)
           {
               mAuth.createUserWithEmailAndPassword(email,password)
                   .addOnCompleteListener{
                       if(it.isSuccessful){
                           val user = mkUser(fullName,email)
                           val reference = mDbase.child("users").child(it.result.user.uid)
                               reference.setValue(user).addOnCompleteListener{
                                   if(it.isSuccessful){
                                      startHomeActivity()
                                   } else {
                                      unknownRegisterError(it)
                                   }
                               }
                       } else{

                           unknownRegisterError(it)

                       }
                   }
           }else {
               Log.e("RegisterActivity", "Email is null!!!")
               showTost("Please enter email!!")
               supportFragmentManager.popBackStack()
           }

       } else {
           showTost("Please enter full name and password")
       }
    }

    private fun unknownRegisterError(it: Task<*>) {
        Log.e("RegisterActivity","FAILED to CREATE user profile",it.exception)
        showTost(" Something Wrong happened. Please try again letter ")
    }

    private fun startHomeActivity() {
        startActivity(Intent(this,HomeActivity::class.java))
        finish()
    }

    private fun mkUser(fullName: String,email: String):User{
        val username = mkUsername(fullName)
        return  User(name=fullName,username = username,email = email)
    }

    private fun mkUsername(fullName: String) =
                    fullName.toLowerCase().replace(" ",".")

}


// 1- e-mail, next button

class  EmailFragment :Fragment () {
    private lateinit var mLisener:Listener

    interface Listener
    {
        fun onNext(email:String)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
      return inflater.inflate(R.layout.fragment_register_email,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {

        next_btn.setOnClickListener{
            val email =  email_input.text.toString()
            mLisener.onNext(email)

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mLisener = context as Listener
    }
}

// 2-Full name? password? register button

class  NamePassFragment :Fragment () {

    private lateinit var mLisener: Listener

    interface Listener
    {
        fun onRegister(fullName:String, password:String)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_register_namepass,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {

        register_bnt.setOnClickListener{
            val fullName =  full_name_input.text.toString()
            val password =  pass_input.text.toString()
            mLisener.onRegister(fullName,password)


        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mLisener = context as Listener
    }

}