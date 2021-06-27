package com.example.posnew.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posnew.CartAdapter
import com.example.posnew.CartItem
import com.example.posnew.R
import kotlinx.android.synthetic.main.fragment_cart.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Cart : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var cartAdapter : CartAdapter
    private var ItemProduk : MutableList<CartItem> = mutableListOf(
        CartItem("Dress",200000,"https://i.ibb.co/wBYDxLq/beach.jpg",1),
        CartItem("Dress",200000,"https://i.ibb.co/wBYDxLq/beach.jpg",1)
    )

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

        cartAdapter = CartAdapter(ItemProduk)
        RecyclerViewCart.adapter = cartAdapter
        RecyclerViewCart.layoutManager= LinearLayoutManager(context)

//        var totalHarga = 0
//        for (i in ItemProduk){
//            totalHarga += ItemProduk.get(i).Harga
//        }

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
