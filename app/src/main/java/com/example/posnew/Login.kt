package com.example.posnew

import android.app.ProgressDialog
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private var sp : SoundPool? = null
    private var soundID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var user = intent.getParcelableExtra<User>(EXTRA_USER)
        logMail.setText(user?.Nama)

        //init
        auth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)

        //intent eksplisit
        reg.setOnClickListener {
            val intent_reg = Intent(this, Register::class.java)
            var user = User(logMail.text.toString())
            intent_reg.putExtra(EXTRA_USER, user)
            startActivity(intent_reg)
        }

        signinbutton.setOnClickListener {
            if(logMail.length() == 0){
                logMail.error = "Email must Not be Empty"
            } else if(logPass.length() == 0){
                logPass.error = "Password must Not be Empty"
            } else{
                if(logMail.text.isEmailValid()){
                    if(soundID != 0){
                        sp?.play(soundID, 0.99f, .99f, 1, 0, .99f)
                    }
                    auth()
                }else{
                    logMail.error = "email is not valid"
                }
            }
        }

        logMail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                logMail.error = "Please Enter Valid Email Address"
                if (logMail.text.isEmailValid())
                    logMail.error = null
            }
        })

        forgotPass.setOnClickListener {
            Snackbar.make(it, "Sorry Forgot Password doesn't available yet", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
        }
    }

    private fun auth(){
        var email = logMail.text.toString()
        var pass = logPass.text.toString()

        progressDialog.setMessage("Mohon Menunggu...")
        progressDialog.setTitle("Login")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                progressDialog.dismiss()
                sendUserToActivity()
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
            }
            else{
                progressDialog.dismiss()
                Toast.makeText(this, "Login Gagal, Password atau Username Salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendUserToActivity() {
        val activity = Intent(this, ActivityFragment::class.java)
        activity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(activity)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_FULLNAME, logMail.text.toString())
        outState.putString(EXTRA_PASSWORD, logPass.text.toString())
        outState.putBoolean(EXTRA_STATUS, stayLogged.isChecked)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logMail.setText(savedInstanceState.getString(EXTRA_FULLNAME, "FULL_NAME"))
        logPass.setText(savedInstanceState.getString(EXTRA_PASSWORD, "PASSWORD"))
        stayLogged.isChecked = savedInstanceState.getBoolean(EXTRA_STATUS, false)
    }

    override fun onStart() {
        super.onStart()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            createNewSoundPool()
        }
        else{
            createOldSoundPool()
        }
        sp?.setOnLoadCompleteListener { soundPool, id, status ->
            if(status != 0){
                Toast.makeText(this, "Gagal Load", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "Load Berhasil", Toast.LENGTH_SHORT).show()
            }
        }
        soundID = sp?.load(this, R.raw.intro, 1) ?: 0
    }

    private fun createOldSoundPool() {
        sp = SoundPool(15, AudioManager.STREAM_MUSIC, 0)
    }

    private fun createNewSoundPool() {
        sp = SoundPool.Builder()
                .setMaxStreams(15)
                .build()
    }

    override fun onStop(){
        super.onStop()
        sp?.release()
        sp = null
    }
}