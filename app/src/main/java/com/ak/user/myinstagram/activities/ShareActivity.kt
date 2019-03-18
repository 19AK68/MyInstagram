package com.ak.user.myinstagram.activities


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.ak.user.myinstagram.R
import com.ak.user.myinstagram.utils.CameraHelper
import com.ak.user.myinstagram.utils.GlideApp
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {

    private val TAG = "ShareActivity"
    private  lateinit var mCameraHelper: CameraHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
      //  setupButtonNavigation()
        Log.d(TAG,"onCteate")

        mCameraHelper = CameraHelper(this)
        mCameraHelper.takeCameraPicture()
        back_image.setOnClickListener{finish()}

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==mCameraHelper.REQUEST_CODE && resultCode == RESULT_OK ){
            GlideApp.with(this).load(mCameraHelper.imageUri).centerCrop().into(post_image)
        }


    }

}
