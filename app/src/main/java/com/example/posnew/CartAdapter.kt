package com.example.posnew

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CartAdapter(data : ArrayList<CartItem>): RecyclerView.Adapter<CartAdapter.myHolder>() {
    private var myData = data
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
            .inflate(R.layout.item_cart,parent,false)
        return myHolder(inflate)
    }

    override fun getItemCount(): Int = myData.size

    override fun onBindViewHolder(holder: myHolder, position: Int) {

        val data = myData[position]
        var count = data.JumlahProduk

        holder.tambah.setOnClickListener{
            count += 1
            holder.jumlahProduk.text = ""+count
            holder.Harga.text = (data.Harga.toString().toInt()*count).toString()
        }

        holder.kurang.setOnClickListener{
            if(count == 1){
                holder.jumlahProduk.text = "1"
                holder.Harga.text = (data.Harga.toString().toInt()*count).toString()
            }
            else{
                count -= 1
                holder.jumlahProduk.text = ""+count
                holder.Harga.setText(data.Harga.toString().toInt()*count)
            }
        }

        holder.hapus.setOnClickListener {
            var newPosition = holder.adapterPosition
            myData.removeAt(newPosition)
            notifyItemRemoved(newPosition);
            notifyItemRangeChanged(newPosition, myData.size);
        }

        holder.NamaProduk.text = data.NamaProduk
        holder.Harga.text = (data.Harga.toString().toInt()*count).toString()
        holder.Jumlah.text = data.JumlahProduk.toString()

        var imageUri = Uri.parse(data.GambarProduk)
        Picasso.get().load(imageUri).into(holder.GambarProduk)
    }
}