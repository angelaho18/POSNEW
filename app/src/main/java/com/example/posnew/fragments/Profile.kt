package com.example.posnew.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import com.example.posnew.ActivityFragment
import com.example.posnew.R
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val REQUEST_CODE = 18

class Profile : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val prefFileName = "MyFilepref1"
    private var i = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val image = data?.extras?.get("data") as Bitmap
            gambarProfile.setImageBitmap(image)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnPic = view.findViewById<Button>(R.id.buttonPic)
        btnPic.setOnClickListener {
            val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicIntent, REQUEST_CODE)
        }

        val outBtn = view.findViewById<Button>(R.id.LogoutBut)
        outBtn.setOnClickListener {
            val intentIn = Intent(context, ActivityFragment::class.java)
            startActivity(intentIn)
        }

        return view
    }

    private fun writeFileInternal() {
        val fullname = view?.findViewById<EditText>(R.id.full_name)
        val mail = view?.findViewById<EditText>(R.id.emailAddress)
        var output = context?.openFileOutput("dataUser.txt", Context.MODE_PRIVATE)?.apply {
            write("${fullname?.text}+${mail?.text}".toByteArray())
            close()
        }
        var myFile = File(context?.filesDir, "dataUser.txt")
        Log.w("OK", myFile.absolutePath)
        Toast.makeText(context, "File Save", Toast.LENGTH_SHORT).show()
    }

    private fun readFileInternal() {
        val fullname = view?.findViewById<EditText>(R.id.full_name)
        val mail = view?.findViewById<EditText>(R.id.emailAddress)
        fullname?.text?.clear()
        mail?.text?.clear()
        try {
            var input = context?.openFileInput("dataUser.txt")?.apply {
                bufferedReader().useLines {
                    for (text in it.toList()) {
                        var fname = ""
                        var em = ""
                        var x = 0
                        for (i in text) {
                            if (i == '+') x = 1
                            if (x == 0) fname += i
                            else if (x == 1 && i != '+') em += i
                        }
                        fullname?.setText("${fname}")
                        mail?.setText("${em}")
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            fullname?.setText("File Not Found")
            mail?.setText("File Not Found")
        } catch (e: IOException) {
            fullname?.setText("File Can't be Read")
            mail?.setText("File Not Found")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}