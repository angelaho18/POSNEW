package com.example.posnew

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pointofsale.presenter.regispresenterInterface
import com.example.posnew.fragments.Profile
import com.example.posnew.presenter.regisPresenter
import com.example.posnew.view.regisviewInterface
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File


class Register : AppCompatActivity(), regisviewInterface {

    private lateinit var regispresenter: regispresenterInterface
    var granted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //init
        regispresenter = regisPresenter(this)

        var user = intent.getParcelableExtra<User>(EXTRA_USER)
        fullName.setText(user?.Nama)

        //intent eksplisit
        log.setOnClickListener {
            val intent_login = Intent(this, MainActivity::class.java)
            var user = User(fullName.text.toString())
            intent_login.putExtra(EXTRA_USER, user)
            startActivity(intent_login)
        }

        signup.setOnClickListener {
//            regispresenter.regis(fullName.text.toString(),emailAddress.text.toString(),password.text.toString())
            if (fullName.length() == 0) {
                fullName.error = "Please input your FullName"
            } else if (emailAddress.length() == 0) {
                emailAddress.error = "Please input your Email Address"
            } else if (password.length() == 0) {
                password.error = "Please input your Password"
            } else {
                if (emailAddress.text.isEmailValid()) {
//                    val intent_profile = Intent(this, Profile::class.java)
//                    var user = User(fullName.text.toString(), emailAddress.text.toString())
//                    intent_profile.putExtra(EXTRA_USER, user)
                    if (isExternalStorageReadable()) {
                        writeFileExternal()
                    }
                } else {
                    emailAddress.error = "Please Enter Valid Email Address"
                }
            }
        }

        emailAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                emailAddress.error = "Please Enter Valid Email Address"
                if (emailAddress.text.isEmailValid())
                    emailAddress.error = null
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_FULLNAME, fullName.text.toString())
        outState.putString(EXTRA_EMAIL, emailAddress.text.toString())
        outState.putString(EXTRA_PASSWORD, password.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        fullName.setText(savedInstanceState.getString(EXTRA_FULLNAME))
        emailAddress.setText(savedInstanceState.getString(EXTRA_EMAIL))
        password.setText(savedInstanceState.getString(EXTRA_PASSWORD))
    }

    override fun regissuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun regiserror(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            123 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent_profile = Intent(this, Profile::class.java)
                    Toast.makeText(this,
                        "Registrasi Berhasil, Hoorayy!!!",
                        Toast.LENGTH_SHORT).show()
                    startActivity(intent_profile)
                } else
                    Toast.makeText(this,
                        "Registrasi Gagal, Silahkan coba lagi...",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isExternalStorageReadable(): Boolean{
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ){
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 123)
        }
        var state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state)
            return true

        return false
    }

    private fun writeFileExternal() {
        var myDir = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.toURI())
        var data = "${fullName.text}\n${emailAddress.text} "

        if (!myDir.exists()) {
            myDir.mkdir()
        }
        File(myDir, "regisData.txt").apply {
            writeText(data)
        }
    }
}