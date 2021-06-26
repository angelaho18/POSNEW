package com.example.posnew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.example.posnew.fragments.*
import com.example.posnew.fragments.List
import kotlinx.android.synthetic.main.bottom_navigation.*

class ActivityFragment : AppCompatActivity(), InterfaceFragment {
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
        fab.setOnClickListener {
            newTransaction(Add())
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
        super.onSaveInstanceState(outState, outPersistentState)
    }
}