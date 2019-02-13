package com.ak.user.myinstagram.activities


import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.Editable
import android.util.Log
import android.widget.TextView
import com.ak.user.myinstagram.R
import com.ak.user.myinstagram.models.User
import com.ak.user.myinstagram.views.PasswordDialod
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.io.IOException

import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity(),PasswordDialod.Listener {


    private val TAG = "EditProfileActivity"
    private lateinit var mUser: User
    private lateinit var mPandingUser: User
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mStorage: StorageReference
    private val TAKE_PICTURE_REQUEST_CODE = 1
    val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    private lateinit var mImageUri: Uri
    private lateinit var mCurrentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        Log.d(TAG, "onCreate")

        close_image.setOnClickListener{ finish()  }
        save_image.setOnClickListener{updateProfile()}
        change_photo_text.setOnClickListener{takeCameraPicture()}

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance().reference

        mDatabase.child("users").child(mAuth.currentUser!!.uid).addListenerForSingleValueEvent(ValueEventListenerAdapter {
               mUser =  it.getValue(User::class.java)!!
                name_input.setText(mUser.name,TextView.BufferType.EDITABLE)
                username_input.setText(mUser.username,TextView.BufferType.EDITABLE)
                website_input.setText(mUser.website,TextView.BufferType.EDITABLE)
                bio_input.setText(mUser.bio,TextView.BufferType.EDITABLE)
                email_input.setText(mUser.email,TextView.BufferType.EDITABLE)
                phone_input.setText(mUser.phone?.toString(),TextView.BufferType.EDITABLE)
                profile_image.loadUserPhoto(mUser.photo)

        })
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = simpleDateFormat.format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }


    private fun takeCameraPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val imageFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                imageFile?.also {
                    mImageUri = FileProvider.getUriForFile(
                        this,
                        "com.ak.user.myinstagram.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
                    startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE)

                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uid = mAuth.currentUser!!.uid
            val ref = mStorage.child("users/$uid/photo")
             ref.putFile(mImageUri).addOnCompleteListener{
                if(it.isSuccessful){
                   ref.downloadUrl.addOnCompleteListener{ it ->
                       val photoUrl=it.result.toString()
                    mDatabase.child("users/$uid/photo").setValue(photoUrl).addOnCompleteListener{
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
            mAuth.currentUser!!.reauthenticate(credential){
                    mAuth.currentUser!!.updateEmail(mPandingUser.email){
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

        mDatabase.updateUser(mAuth.currentUser!!.uid,updatesMap){
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

    private  fun DatabaseReference.updateUser(uid:String,updates:Map<String,Any?>,onSuccess: () -> Unit){

        child("users").child(uid).updateChildren(updates)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                   onSuccess()
                } else {
                    showTost(it.exception!!.message!!)
                }
            }
    }

    private fun FirebaseUser.updateEmail(email:String,onSuccess: ()-> Unit){
        updateEmail(email).addOnCompleteListener{

            if(it.isSuccessful){
                onSuccess()
            } else {
                showTost(it.exception!!.message!!)
            }
        }

    }
    private fun FirebaseUser.reauthenticate(credential: AuthCredential,onSuccess: ()-> Unit){
        reauthenticate(credential).addOnCompleteListener{
            if(it.isSuccessful){
                onSuccess()
            } else {
                showTost(it.exception!!.message!!)
            }
        }
    }

}
