package com.ak.user.myinstagram.activities


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.ak.user.myinstagram.R
import com.ak.user.myinstagram.utils.CameraHelper
import com.ak.user.myinstagram.utils.FirebaseHelper
import com.ak.user.myinstagram.utils.GlideApp
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {

    private val TAG = "ShareActivity"
    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebaseHelper: FirebaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        //  setupButtonNavigation()
        Log.d(TAG, "onCteate")

        mFirebaseHelper = FirebaseHelper(this)
        mCamera = CameraHelper(this)
        mCamera.takeCameraPicture()
        back_image.setOnClickListener { finish() }
        share_text.setOnClickListener { share() }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCamera.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                GlideApp.with(this).load(mCamera.imageUri).centerCrop().into(post_image)
            } else {
                finish()
            }
        }
    }

    private fun share() {
        val imageUrl = mCamera.imageUri
        if (imageUrl != null) {
            val uid = mFirebaseHelper.mAuth.currentUser!!.uid
            mFirebaseHelper.mStorage.child("users").child(uid).child("images")
                .child(imageUrl.lastPathSegment).putFile(imageUrl).addOnCompleteListener{
                    if(it.isSuccessful){
                        mFirebaseHelper.mDatabase.child("images").child(uid).push().
                            setValue(imageUrl.toString())
                            .addOnCompleteListener{
                                if(it.isSuccessful){
                                    startActivity(Intent(this,ProfileActivity::class.java))
                                    finish()
                                }else {
                                    showTost(it.exception!!.message!!)
                                }
                            }
                    } else {
                        showTost(it.exception!!.message!!)
                    }
                }
            //upload image to user <- storage
            // add image user images <-db

        }

    }

}
