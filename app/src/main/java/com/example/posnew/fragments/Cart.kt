package com.example.posnew.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posnew.CartAdapter
import com.example.posnew.CartItem
import com.example.posnew.PREF_NAME
import com.example.posnew.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter
import java.lang.reflect.Type


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Cart : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var cartAdapter : CartAdapter
    private lateinit var cartSharedPref: SharedPreferences
    private var cartData = arrayListOf<CartItem>()
//    private var ItemProduk : MutableList<CartItem> = mutableListOf(
//        CartItem("Dress",200000,"https://i.ibb.co/wBYDxLq/beach.jpg",1),
//        CartItem("Shirt",100000,"https://i.ibb.co/wBYDxLq/beach.jpg",1)
//    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //hasilScan.text= EXTRA_SCAN
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val RecyclerViewCart = view.findViewById<RecyclerView>(R.id.RecyclerViewCart)
        var totalPrice = view.findViewById<TextView>(R.id.totalPrice)

        val gson = Gson()

        cartSharedPref = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)!!
        var getJson = cartSharedPref.getString("cart", "")
        if(getJson != ""){
            val type: Type = object : TypeToken<ArrayList<CartItem?>?>() {}.type
            var obj = gson.fromJson<ArrayList<CartItem>>(getJson, type)
            cartData.addAll(obj)
        }

        var item = arguments?.getParcelable<CartItem>("item")
        Log.d("cart", "onCreateView: $item")
        if(item != null){
            cartData.add(item!!)
            var jsonString = gson.toJson(cartData)
            cartSharedPref.edit {
                putString("cart", jsonString)
                apply()
            }
        }

        cartAdapter = CartAdapter(view.context, cartData)
        RecyclerViewCart.adapter = cartAdapter
        RecyclerViewCart.layoutManager= LinearLayoutManager(context)

        var total = 0
        for (i in cartData){
            total += i.Harga
        }
        val totalHarga  = view.findViewById<TextView>(R.id.totalPrice)
        totalHarga.text = total.toString()

//        var totalHarga = 0
//        for (i in 0 until ItemProduk.size){
//            totalHarga += (ItemProduk[i].Harga) * (ItemProduk[i].JumlahProduk)
//        }
//
//        totalPrice.setText(totalHarga.toString())

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Cart().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
