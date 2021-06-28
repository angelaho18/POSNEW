package com.example.posnew

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewsAdapter (data : MutableList<News>) : RecyclerView.Adapter<NewsAdapter.FeedHolder>() {
    private var myData = data

    class FeedHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val Informasi = view.findViewById<TextView>(R.id.information)
        val Pic = view.findViewById<ImageView>(R.id.FeedGambar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return FeedHolder(inflate)
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        holder.Informasi.setText(myData.get(position).informasi)
        Picasso.get().load(myData.get(position).gambar).into(holder.Pic)

        holder.Pic.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(myData.get(position).link))

            val context = holder.itemView.context
            context.startActivity(i)
            
        }
    }

    override fun getItemCount(): Int = myData.size
}