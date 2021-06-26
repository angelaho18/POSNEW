package com.example.posnew.presenter

import com.example.pointofsale.presenter.regispresenterInterface
import com.example.posnew.model.User
import com.example.posnew.view.regisviewInterface

class regisPresenter (internal var regisviewInterface: regisviewInterface):
    regispresenterInterface {
    override fun regis(fullname: String, email: String, password: String) {
        val user = User(fullname,email,password)
        val regiscode = user.isvalid()
        if(regiscode == 3)
            regisviewInterface.regiserror("Fullname empty")
        else if(regiscode == 0)
            regisviewInterface.regiserror("Email Empty")
        else if(regiscode == 1)
            regisviewInterface.regiserror("Email pattern not match")
        else if(regiscode == 2)
            regisviewInterface.regiserror("password min. 1 character")
        else
            regisviewInterface.regissuccess("Register success")
    }
}