package com.example.posnew.Room

import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.NumberFormat
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.posnew.AlertUtils
import com.example.posnew.R
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import java.util.*

class ProductAdapter(private val query: String?, private val db: ProductViewModel) :
    RecyclerView.Adapter<ProductAdapter.ItemHolder>() {
    private var found = false
    private var items = emptyList<Product>()

    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view){
        val ProductName = view.findViewById<TextView>(R.id.product_name)
        val Quantity = view.findViewById<TextView>(R.id.quantity)
        val Price = view.findViewById<TextView>(R.id.price)
        val row = view.findViewById<LinearLayout>(R.id.row)
        val ProductImg = view.findViewById<ImageView>(R.id.GambarProduk)
        val barcodeid = view.findViewById<TextView>(R.id.barcode)
        val mMenu = view.findViewById<ImageView>(R.id.mMenus)

        fun popupMenus(view: View, item: Product, db: ProductViewModel) {
            val popupMenus = PopupMenu(itemView.context,view)
            popupMenus.inflate(R.menu.floating_context_menu_list)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editData-> {
                        AlertUtils.updataDialog(view, item, db)
                        Toast.makeText(itemView.context, "Edit Button", Toast.LENGTH_LONG).show()
                        true
                    }
                    R.id.deleteData->{
                        doAsync{
//                            db.productDao().deleteData(item)
                            db.delete(item.id)
//                            uiThread {
//                                fragment_list.getData(db)
//                            }
                        }
//                        Toast.makeText(itemView.context, "Delete Button", Toast.LENGTH_LONG).show()
                        true
                    }
                    else-> true
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu,true)
        }

        fun changeColor(item: Product, term: String?): Boolean {
            if (term != null) {
                if (item.ProductName.contains(term.toString())) {
                    Toast.makeText(itemView.context, "hey, you find me", Toast.LENGTH_LONG).show()
                    row.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                    return true
                } else {
                    Toast.makeText(
                        itemView.context,
                        "Sorry, we can't find the item",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
        return ItemHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var item = items[position]
        holder.ProductName.text = item.ProductName
        holder.Quantity.text = item.Quantity.toString()
        holder.barcodeid.text = item.BarcodeID
        if (!found) found = holder.changeColor(item, query)

        holder.Price.text = rupiah(item.Price)
        holder.mMenu.setOnClickListener {
            holder.popupMenus(it, item, db)
        }

        var imageUri = Uri.parse(item.ProductPic)
//        Glide.with(holder.view).load(imageUri).into(holder.ProductImg)
        Picasso.get().load(imageUri).into(holder.ProductImg)

//        var bitmapData = item.ProductPic
//        var bitmap = ImageBitmapString.StringToBitMap(bitmapData);
////content://com.android.providers.media.documents/document/image%3A35652
//        holder.ProductImg.setImageBitmap(bitmap)

//        val data = items.get(position)
//
//        val angka = data.salePrice
//        val getfloat = "f"
//        val gabung = angka + getfloat
//
//        holder.ProductName.text = "${data.title}"
//        holder.Quantity.text = data.storeID
////        Log.d("WOW", data.salePrice.toInt().toString())
//        holder.Price.text = rupiah(gabung.toFloat().toInt() * 14500)
////        holder.Price.text = rupiah(1000)
//        Picasso.get().load(data.thumb).into(holder.view.GambarProduk)
    }

    override fun getItemCount(): Int = items.size

    fun rupiah(Price: Int): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(Price).toString()
    }

    fun setData(list: List<Product>){
        this.items = list
        notifyDataSetChanged()
    }
}

