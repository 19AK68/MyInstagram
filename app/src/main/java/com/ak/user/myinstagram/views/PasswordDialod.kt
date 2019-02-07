package com.ak.user.myinstagram.views

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.ak.user.myinstagram.R
import kotlinx.android.synthetic.main.dialog_password.view.*

class PasswordDialod: DialogFragment (){
    private lateinit var mListener:Listener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_password,null)
        return AlertDialog.Builder(context!!)
            .setView(view)
            .setPositiveButton(android.R.string.ok,{_ ,_ ->
                mListener.onPasswordConfirm(view.pass_input.text.toString())
            })
            .setNegativeButton(android.R.string.cancel, { _ ,_ ->
                // do nothing
            })
            .setTitle(R.string.please_enter_password)
            .create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as Listener
    }

    interface Listener {
        fun onPasswordConfirm(password:String)
    }
}