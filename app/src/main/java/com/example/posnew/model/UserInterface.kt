package com.example.posnew.model

interface UserInterface {
    val fullname:String
    val email: String
    val password:String
//    val isvalid:Boolean
    fun isvalid():Int

}