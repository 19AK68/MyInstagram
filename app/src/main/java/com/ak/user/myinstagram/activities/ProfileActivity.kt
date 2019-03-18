package com.ak.user.myinstagram.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import com.ak.user.myinstagram.R
import com.ak.user.myinstagram.models.User
import com.ak.user.myinstagram.utils.FirebaseHelper
import com.ak.user.myinstagram.utils.GlideApp
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

        images_recyler.layoutManager = GridLayoutManager(this,3)
        mFirebaseHelper.mDatabase.child("images").child(mFirebaseHelper.mAuth.currentUser!!.uid)
            .addValueEventListener(ValueEventListenerAdapter{
               val images = it.children.map{ it.getValue(String::class.java)!!}
                images_recyler.adapter = ImagesAdapter(images+images+images+images)
            })

    }
}

class ImagesAdapter(private val images: List<String>):RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){
    class ViewHolder (val image:ImageView):RecyclerView.ViewHolder(image)

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val image = LayoutInflater.from(parent.context)
            .inflate(R.layout.images_item,parent,false) as ImageView
        return ViewHolder(image)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.loadImage(images[position])
    }

    private fun ImageView.loadImage(image:String){
        GlideApp.with(this).load(image).centerCrop().into(this)
    }
}

class SquareImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
