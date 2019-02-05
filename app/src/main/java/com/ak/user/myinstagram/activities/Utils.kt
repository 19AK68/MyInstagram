package com.ak.user.myinstagram.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.util.Log.e
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.time.Duration


class ValueEventListenerAdapter(val handler: (DataSnapshot)->Unit) : ValueEventListener {

    private val TAG = "ValueEventListenerAdapt"

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }

    override fun onCancelled(error: DatabaseError) {

        Log.e(TAG, "onCancelled ", error.toException())

    }
}

fun Context.showTost(text:String,duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(this, text,duration).show()
}

