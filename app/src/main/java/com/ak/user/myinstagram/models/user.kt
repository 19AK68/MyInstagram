package com.ak.user.myinstagram.models

data class User (val name:String="",val email:String="", val username:String="",
                 val website:String?=null,
                 val bio:String?=null,
                 val phone:Long? = null,
                 val photo:String?=null)