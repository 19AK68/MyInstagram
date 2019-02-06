package com.ak.user.myinstagram.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ak.user.myinstagram.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginActivity : AppCompatActivity(), KeyboardVisibilityEventListener, View.OnClickListener {

    private val TAG="LoginActivity"
    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(TAG,"onCreate")

        KeyboardVisibilityEvent.setEventListener(this,this)

        coordinateBtnAndInputs(login_bnt,email_input,pass_input)

        login_bnt.setOnClickListener(this)
        create_account_text.setOnClickListener(this)

        mAuth= FirebaseAuth.getInstance()
    }

    override fun onClick(view: View) {
        when(view.id)
        {
            R.id.login_bnt -> {
                val email = email_input.text.toString()
                val password =  pass_input.text.toString()
                if(validite(email,password)){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                        if(it.isSuccessful){
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    showTost("Please enter email and password")
                }
            }
            R.id.create_account_text ->{
                startActivity(Intent(this, RegisterActivity::class.java))

            }
        }
    }

    override fun onVisibilityChanged(isKeyboardOpen: Boolean) {
        if(isKeyboardOpen){

            create_account_text.visibility = View.GONE
        } else {

            create_account_text.visibility = View.VISIBLE
        }
    }



    private fun validite(email:String,password:String)= email.isNotEmpty()&& password.isNotEmpty()


}
