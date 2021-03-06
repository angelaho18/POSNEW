package com.example.posnew

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class CartAdapter(context: Context, data: ArrayList<CartItem>): RecyclerView.Adapter<CartAdapter.myHolder>() {
    private val context = context
    private var myData = data
    private val gson = Gson()
    private lateinit var cartSharedPref: SharedPreferences
    private lateinit var listener: OnItemsClickListener

    class myHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val GambarProduk = itemView.findViewById<ImageView>(R.id.GambarProduk)
        val NamaProduk = itemView.findViewById<TextView>(R.id.NamaProduk)
        val Harga = itemView.findViewById<TextView>(R.id.harga)
        val Jumlah = itemView.findViewById<TextView>(R.id.jumlahProduk)
        val tambah = itemView.findViewById<Button>(R.id.addCart)
        val kurang = itemView.findViewById<Button>(R.id.reduceCart)
        val jumlahProduk = itemView.findViewById<TextView>(R.id.jumlahProduk)
        val hapus = itemView.findViewById<ImageButton>(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return myHolder(inflate)
    }

    override fun getItemCount(): Int = myData.size

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        val data = myData[position]
        var count = data.JumlahProduk
        cartSharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)!!

        holder.tambah.setOnClickListener{
            count += 1
            holder.jumlahProduk.text = ""+count
            holder.Harga.text = (data.Harga.toString().toInt()*count).toString()
            data.JumlahProduk = holder.Jumlah.text.toString().toInt()
            sharedPref(myData)
            grandTotal()
            if(listener != null) listener.onItemClick(data)
        }

        holder.kurang.setOnClickListener{
            if(count == 1){
                holder.jumlahProduk.text = "1"
                holder.Harga.text = (data.Harga.toString().toInt()*count).toString()
            }
            else{
                count -= 1
                holder.jumlahProduk.text = ""+count
                holder.Harga.text = (data.Harga.toString().toInt()*count).toString()
            }
            data.JumlahProduk = holder.Jumlah.text.toString().toInt()
//            notifyItemChanged(position)
            sharedPref(myData)
            grandTotal()
            if(listener != null) listener.onItemClick(data)
        }

        holder.hapus.setOnClickListener {
            var newPosition = holder.adapterPosition
            myData.removeAt(newPosition)
            notifyItemRemoved(newPosition);
            notifyItemRangeChanged(newPosition, myData.size);
            grandTotal()
            sharedPref(myData)
            if(listener != null) listener.onItemClick(data)
        }

        holder.NamaProduk.text = data.NamaProduk
        holder.Harga.text = (data.Harga.toString().toInt()*count).toString()
        holder.Jumlah.text = data.JumlahProduk.toString()

        var imageUri = Uri.parse(data.GambarProduk)
        Picasso.get().load(imageUri).into(holder.GambarProduk)
    }

    fun sharedPref(data: ArrayList<CartItem>){
        var jsonString = gson.toJson(data)
        cartSharedPref.edit {
            putString("cart", jsonString)
            apply()
        }
    }

    fun grandTotal(): Int{
        var totalprice = 0
        for (i in myData){
            totalprice += i.Harga * i.JumlahProduk
        }
        return totalprice
    }

    fun pay(){
        myData.removeAll(myData)
        cartSharedPref.edit().clear().apply()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemsClickListener?) {
        this.listener = listener!!
    }

    interface OnItemsClickListener {
        fun onItemClick(cartItem: CartItem)
    }
}