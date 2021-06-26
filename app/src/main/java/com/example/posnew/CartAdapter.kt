package com.example.posnew

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CartAdapter(data : MutableList<CartItem>): RecyclerView.Adapter<CartAdapter.myHolder>() {
    private var myData = data
    class myHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val GambarProduk = itemView.findViewById<ImageView>(R.id.GambarProduk)
        val NamaProduk = itemView.findViewById<TextView>(R.id.NamaProduk)
        val Harga = itemView.findViewById<TextView>(R.id.harga)
        val Jumlah = itemView.findViewById<TextView>(R.id.jumlahProduk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart,parent,false)
        return myHolder(inflate)
    }

    override fun getItemCount(): Int = myData.size

    override fun onBindViewHolder(holder: myHolder, position: Int) {

        holder.NamaProduk.setText(myData.get(position).NamaProduk)
        holder.Harga.setText(myData.get(position).Harga.toString())
        holder.Jumlah.setText(myData.get(position).JumlahProduk.toString())
        Picasso.get().load(myData.get(position).GambarProduk).into(holder.GambarProduk)
    }
}