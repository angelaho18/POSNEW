package com.example.posnew

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pointofsale.presenter.regispresenterInterface
import com.example.posnew.fragments.Profile
import com.example.posnew.presenter.regisPresenter
import com.example.posnew.view.regisviewInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File


class Register : AppCompatActivity(), regisviewInterface {

    private lateinit var regispresenter: regispresenterInterface
    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    var granted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //init
        regispresenter = regisPresenter(this)
        progressDialog = ProgressDialog(this)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.reference

        var user = intent.getParcelableExtra<User>(EXTRA_USER)
        emailAddress.setText(user?.Nama)

        //intent eksplisit
        log.setOnClickListener {
            val intent_login = Intent(this, MainActivity::class.java)
            var user = User(fullName.text.toString(), emailAddress.text.toString())
            intent_login.putExtra(EXTRA_USER, user)
            startActivity(intent_login)
        }

        signup.setOnClickListener {
            if (fullName.text.toString().trim() == "") {
                fullName.error = "Please input your FullName"
            } else if (emailAddress.text.toString().trim() == "") {
                emailAddress.error = "Please input your Email Address"
            } else if (password.text.toString().trim() == "") {
                password.error = "Please input your Password"
            } else {
                if (emailAddress.text.isEmailValid()) {
                    auth()
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

    private fun auth() {
        var name = fullName.text.toString()
        var email = emailAddress.text.toString()
        var pass = password.text.toString()

        if (password.length() >= 6){
            progressDialog.setMessage("Mohon Menunggu...")
            progressDialog.setTitle("Registrasi")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            saveToRtDb(name, email, pass)
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseUser = it.result?.user!!
                    progressDialog.dismiss()
                    sendUserToActivity(firebaseUser, email)
                    Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()

                    saveToRtDb(name, email, pass)
                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(this, "Registrasi Gagal ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            password.error = "Password must at least 6 character"
        }
    }

    private fun saveToRtDb(name: String, email: String, pass: String) {
        firebaseUser = auth.currentUser!!
        Log.d("Firebase RTDB", "saveToRtDb: ${firebaseUser.uid}")
        var hashMap = HashMap<String, String>()
        hashMap["name"] = name
        hashMap["email"] = email
        hashMap["password"] = pass

        databaseReference.child("users")
            .child(firebaseUser.uid)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show()
                Log.d("Firebase RealTime Database", "saveToRtDb: User berhasil dimasukkan")
            }
            .addOnFailureListener {
                Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show()
                Log.d("Firebase RealTime Database", "saveToRtDb: User Gagal dimasukkan")
            }
    }

    private fun sendUserToActivity(user: FirebaseUser, email: String) {
        val activity = Intent(this, ActivityFragment::class.java)
        activity.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("user_id", user.uid)
            putExtra("email", email)
        }
        startActivity(activity)
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