package com.ak.user.myinstagram.activities




import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.TextView
import com.ak.user.myinstagram.R
import com.ak.user.myinstagram.models.User
import com.ak.user.myinstagram.utils.CameraPictureTaker
import com.ak.user.myinstagram.utils.FirebaseHelper
import com.ak.user.myinstagram.utils.ValueEventListenerAdapter
import com.ak.user.myinstagram.views.PasswordDialod

import com.google.firebase.auth.EmailAuthProvider

import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : AppCompatActivity(),PasswordDialod.Listener {

    private val TAG = "EditProfileActivity"
    private lateinit var mUser: User
    private lateinit var mPandingUser: User
    private lateinit var mfirebaseHelper: FirebaseHelper



    private lateinit var mCurrentPhotoPath: String

    private lateinit var mCameraPictureTaker: CameraPictureTaker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        Log.d(TAG, "onCreate")

        mCameraPictureTaker = CameraPictureTaker(this)
        mfirebaseHelper = FirebaseHelper(this)

        close_image.setOnClickListener{ finish()  }
        save_image.setOnClickListener{updateProfile()}
        change_photo_text.setOnClickListener{mCameraPictureTaker.takeCameraPicture()}



        mfirebaseHelper.currentUserReferens().addListenerForSingleValueEvent(ValueEventListenerAdapter {
            mUser = it.getValue(User::class.java)!!
            name_input.setText(mUser.name)
            username_input.setText(mUser.username)
            website_input.setText(mUser.website)
            bio_input.setText(mUser.bio)
            email_input.setText(mUser.email)
            phone_input.setText(mUser.phone?.toString() )
            profile_image.loadUserPhoto(mUser.photo)

        })
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCameraPictureTaker.REQUEST_CODE && resultCode == RESULT_OK) {

            mfirebaseHelper.currenUserStorage()
                .putFile(mCameraPictureTaker.imageUri!!)
                .addOnCompleteListener{
                if(it.isSuccessful){
                    mfirebaseHelper.currenUserStorage()
                        .downloadUrl.addOnCompleteListener{ it ->
                        val photoUrl=it.result.toString()
                        mfirebaseHelper.updateUserPhoto(photoUrl){
                            if (it.isSuccessful) {
                                mUser = mUser.copy(photo = photoUrl)
                                profile_image.loadUserPhoto(mUser.photo)
                            }
                        }

                    }
                }else{
                    showTost(it.exception!!.message!!)
                }
            }
        }
    }


    private fun updateProfile() {
        //get user from input
        //validate
         mPandingUser = readInputs()


        val error = validate(mPandingUser)
        if(error == null)
        {
            if(mPandingUser.email == mUser.email)
            {
                updateUser(mPandingUser)
            } else {
                // password
                PasswordDialod().show(supportFragmentManager,"password_dialog")
            }
        } else {
            showTost(error)
        }
    }


    private fun readInputs(): User{

        return User(
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            email = email_input.text.toString(),
            website = website_input.text.toStringOrNull(),
            bio = bio_input.text.toStringOrNull(),
            phone = phone_input.text.toString().toLongOrNull()
        )
    }


    override fun onPasswordConfirm(password: String) {
        if(password.isNotEmpty()){
            val credential = EmailAuthProvider.getCredential(mUser.email,password)
           mfirebaseHelper.reauthenticate(credential){
                    mfirebaseHelper.updateEmail(mPandingUser.email){
                        updateUser(mPandingUser)
                    }
            }
        } else {
            showTost("You should enter your passwords")
        }
    }



    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any?>()
        if (user.name != mUser.name) updatesMap["name"] = user.name
        if (user.username != mUser.username) updatesMap["username"] = user.username
        if (user.website != mUser.website) updatesMap["website"] = user.website
        if (user.bio != mUser.bio) updatesMap["bio"] = user.bio
        if (user.email != mUser.email) updatesMap["email"] = user.email
        if (user.phone != mUser.phone) updatesMap["phone"] = user.phone

        mfirebaseHelper.updateUser(updatesMap){
            showTost("Profile saved")
            finish()

        }
    }

    private fun validate(user: User): String? =
        when {
            user.name.isEmpty() -> "Please enter name"
            user.username.isEmpty() -> "Please enter username"
            user.email.isEmpty() -> "Please enter email"
            else -> null
        }


}
