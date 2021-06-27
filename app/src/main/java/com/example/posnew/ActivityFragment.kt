package com.example.posnew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.posnew.Room.Product
import com.example.posnew.Room.ProductViewModel
import com.example.posnew.fragments.*
import com.example.posnew.fragments.List
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.bottom_navigation.*

class ActivityFragment : AppCompatActivity(), InterfaceFragment {
    var scannedResult: String = ""
    private val vm: ProductViewModel by viewModels()

    private lateinit var cartAdapter : CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        //untuk tampilan bottom nav bar
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        newTransaction(Home())

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home1 -> newTransaction(Home())
                R.id.list1 -> newTransaction(List())
                R.id.cart1 -> newTransaction(Cart())
                R.id.person1 -> newTransaction(Profile())
            }
            true
        }
        scan.setOnClickListener {
            run {
                IntentIntegrator(this).initiateScan();
            }

            vm.getAllData().observe(this, Observer {
                for (i in 0 until it.size){
                    if(it[i].BarcodeID === scannedResult){
                        
                    }
                    else{
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Barcode Scan")
                        builder.setMessage("Data not found, please add the item first at list")
                        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                        builder.setPositiveButton("Add Item") { dialog, which ->
                            bottomNavigationView.menu.findItem(R.id.list1).isChecked = true
                            newTransaction(List())

                            dialog.dismiss()
                        }

                        builder.setNegativeButton("Dismiss") { dialog, which ->
                            dialog.dismiss()
                        }
                        builder.show()
                    }
                }
            })

            bottomNavigationView.menu.findItem(R.id.cart1).isChecked = true
            newTransaction(Cart())
        }

        var openNotif = intent.getBooleanExtra(EXTRA_NOTIF, false)
        if(openNotif) {
            bottomNavigationView.menu.findItem(R.id.list1).isChecked = true
            newTransaction(List())
        }
        var reload = intent.getBooleanExtra(EXTRA_RELOAD, false)
        if(reload){
            bottomNavigationView.menu.findItem(R.id.list1).isChecked = true
            newTransaction(List())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                scannedResult = result.contents
                EXTRA_SCAN = scannedResult
            } else {
                EXTRA_SCAN = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun search(searchTerm: String) {
        val bundle = Bundle()
        bundle.putString("query", searchTerm)
        val transaction = this.supportFragmentManager.beginTransaction()
        val listFragment = List()
        listFragment.arguments = bundle
        transaction.replace(R.id.fragmentContainer, listFragment).addToBackStack(null).commit()
    }

    private fun newTransaction(fragment: Fragment) {
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState?.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.let {
            scannedResult = it.getString("scannedResult").toString()
            EXTRA_SCAN = scannedResult
        }
    }
}