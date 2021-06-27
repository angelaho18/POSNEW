package com.example.posnew

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_home.view.*
import kotlin.math.log

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
    }

    override fun getItemCount(): Int = myData.size
}