package com.example.posnew

import android.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.posnew.Room.Product
import com.example.posnew.Room.ProductViewModel
import org.jetbrains.anko.doAsync

object AlertUtils {
    fun updataDialog(view: View, item: Product, db: ProductViewModel){
        val views = View.inflate(view.context, R.layout.layout_pop_up, null)
        val builder = AlertDialog.Builder(view.context)
        builder.setView(views)
        var dialog = builder.create()
        val confirmBtn = views.findViewById<Button>(R.id.bt_add_to_conf)
        val cancelBtn = views.findViewById<Button>(R.id.bt_cancel)
        val name = views.findViewById<EditText>(R.id.inputName)
        val qty = views.findViewById<EditText>(R.id.inputQty)
        val price = views.findViewById<EditText>(R.id.inputPrice)

        confirmBtn.text = "Update"

        name.setText(item.ProductName)
        qty.setText(item.Quantity.toString())
        price.setText(item.Price.toString())

        dialog.show()
        confirmBtn.setOnClickListener {
            doAsync{
                item.ProductName = name.text.toString()
                item.Quantity = qty.text.toString().toInt()
                item.Price = price.text.toString().toInt()
                db.update(item)
            }
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }
}