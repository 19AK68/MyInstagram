package com.ak.user.myinstagram.utils

import android.app.Activity
import com.ak.user.myinstagram.activities.showTost
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FirebaseHelper (private val activity:Activity) {

     val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
     val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
     val mStorage: StorageReference = FirebaseStorage.getInstance().reference

     fun updateUserPhoto(photoUrl:String,onSuccess: () -> Unit){

        mDatabase.child("users/${mAuth.currentUser!!.uid}/photo").setValue(photoUrl).addOnCompleteListener{

            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showTost(it.exception!!.message!!)
            }
        }

    }

    fun updateUser(updates:Map<String,Any?>,onSuccess: () -> Unit){

        mDatabase.child("users").child(mAuth.currentUser!!.uid).updateChildren(updates)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    activity.showTost(it.exception!!.message!!)
                }
            }
    }

     fun updateEmail(email:String, onSuccess: ()-> Unit){
        mAuth.currentUser!!.updateEmail(email).addOnCompleteListener{

            if(it.isSuccessful){
                onSuccess()
            } else {
                activity.showTost(it.exception!!.message!!)
            }
        }

    }
     fun reauthenticate(credential: AuthCredential, onSuccess: ()-> Unit){
        mAuth.currentUser!!.reauthenticate(credential).addOnCompleteListener{
            if(it.isSuccessful){
                onSuccess()
            } else {
                activity.showTost(it.exception!!.message!!)
            }
        }
    }

    fun currentUserReferens():DatabaseReference =  mDatabase.child("users").child(mAuth.currentUser!!.uid)

    fun currenUserStorage():StorageReference = mStorage.child("users/${mAuth.currentUser!!.uid}/photo")

}