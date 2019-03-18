package com.ak.user.myinstagram.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.ak.user.myinstagram.R
import com.ak.user.myinstagram.models.User
import com.ak.user.myinstagram.utils.FirebaseHelper
import com.ak.user.myinstagram.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : BaseActivity(4){
    private val TAG = "ProfileActivity"
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupButtonNavigation()
        Log.d(TAG,"onCreate")

        edit_profile_btn.setOnClickListener{
            val intent= Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        mFirebaseHelper = FirebaseHelper(this)
        mFirebaseHelper.currentUserReferens().addValueEventListener(ValueEventListenerAdapter{
            mUser = it.getValue(User::class.java)!!
            profile_img.loadUserPhoto(mUser.photo)
            username_text.text=mUser.username
        })

    }
}
